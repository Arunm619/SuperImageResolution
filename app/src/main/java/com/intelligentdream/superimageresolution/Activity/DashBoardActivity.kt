package com.intelligentdream.superimageresolution.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.intelligentdream.superimageresolution.Adapter.HistoryAdapter
import com.intelligentdream.superimageresolution.Helpers.IMAGE_PICK_CODE
import com.intelligentdream.superimageresolution.Helpers.PERMISSION_CODE
import com.intelligentdream.superimageresolution.Model.Image
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_dash_board.*


class DashBoardActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: DatabaseReference? = null
    var currentUser: FirebaseUser? = null
    var mStorageRef: StorageReference? = null
    var dialog: AlertDialog? = null
    var imgList = mutableListOf<Image>()
    lateinit var adapter: HistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.intelligentdream.superimageresolution.R.layout.activity_dash_board)
        supportActionBar!!.title = "My DashBoard"

        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth?.currentUser
        mDatabase = FirebaseDatabase.getInstance().reference.child("Users")
        mStorageRef = FirebaseStorage.getInstance().reference.child("Images")

        rv_history.setHasFixedSize(true)
        rv_history.isNestedScrollingEnabled = false

        val llm = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, true)

        rv_history.layoutManager = llm

        val rootRef = mDatabase!!.child(currentUser!!.uid)
        rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val children = snapshot.children
                    for (h in children) {
                        val imObj = h.getValue(Image::class.java)
                        if (imObj != null) {
                            imgList.add(imObj)
                        }
                    }

                }

                adapter = HistoryAdapter(imgList, baseContext)
                rv_history.adapter = adapter
                adapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE



                if (adapter.itemCount == 0) {
                    view_empty.visibility = View.VISIBLE

                } else {
                    view_empty.visibility = View.GONE

                }

            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }


        })

        btn_choose_pic.setOnClickListener {
            pickImageFromGalley()
        }


        createAlertDialog()


    }

    private fun createAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Uploading Image")
        builder.setIcon(R.drawable.processing)
        builder.setMessage("Please Wait Your Image is being Uploaded.")


        dialog = builder.create()


    }


    private fun pickImageFromGalley() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //denied so request
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //already granted
                pickImageFromGalleyUtils()
            }
        } else {
            //lower than M
            pickImageFromGalleyUtils()

        }
    }

    private fun pickImageFromGalleyUtils() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    pickImageFromGalleyUtils()
                } else {
                    //  Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    Snackbar.make(Parentdashboard, "Permission Denied", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {


            //Toast.makeText(this, data?.data.toString(), Toast.LENGTH_LONG).show()
            //get current user and add this image under images

            dialog?.show()
            //pb_loading.visibility = View.VISIBLE


            val userId: String = currentUser?.uid.toString()
            val randomID = mDatabase!!.push().key.toString()

            //first upload original image
            // and add super image default

            mStorageRef = mStorageRef!!.child(userId).child(randomID)

            val originalImageFilePath = mStorageRef!!.child("originalImage.jpg")
            val superImageFilePath = mStorageRef!!.child("superImage.jpg")
            val uri = data?.data
            if (uri != null) {


                val imageUri = Uri.parse(
                    ContentResolver.SCHEME_ANDROID_RESOURCE +
                            "://" + resources.getResourcePackageName(com.intelligentdream.superimageresolution.R.drawable.loading)
                            + '/'.toString() + resources.getResourceTypeName(com.intelligentdream.superimageresolution.R.drawable.loading) +
                            '/'.toString() + resources.getResourceEntryName(com.intelligentdream.superimageresolution.R.drawable.loading)
                )
                superImageFilePath.putFile(imageUri)

                val uploadTask = originalImageFilePath.putFile(uri)


                val urlTask = uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation originalImageFilePath.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        //  Toast.makeText(this, "Uploaded the image success", Toast.LENGTH_LONG).show()

                        val downloadOriginalImageUrl =
                            downloadUri.toString()


                        //  Toast.makeText(this, downloadOriginalImageUrl, Toast.LENGTH_LONG).show()
                        val LinktoOriginalImage = downloadOriginalImageUrl
                        val LinktoSuperImage = getString(R.string.dummylink)

                        val Image = Image(randomID, LinktoOriginalImage, LinktoSuperImage)

                        imgList.add(Image)
                        adapter.notifyDataSetChanged()

                        if (adapter.itemCount == 0) {
                            view_empty.visibility = View.VISIBLE

                        } else {
                            view_empty.visibility = View.GONE

                        }


                        mDatabase!!.child(userId).push().setValue(Image)
                            .addOnCompleteListener { task: Task<Void> ->
                                if (task.isSuccessful) {


                                    dialog?.dismiss()

                                    //   Toast.makeText(this, "updated", Toast.LENGTH_LONG).show()
                                    Snackbar.make(
                                        Parentdashboard,
                                        "File Uploaded. Started Processing.",
                                        Snackbar.LENGTH_LONG
                                    ).show()

                                    // pb_loading.visibility = View.GONE

                                } else {

                                    /*Toast.makeText(

                                        this,
                                        "try again bro ${task.exception.toString().split(":")[0]}",
                                        Toast.LENGTH_LONG
                                    ).show()
*/
                                    // Snackbar.make(rl_signupActivity, task.exception.toString(), Snackbar.LENGTH_LONG).show()

                                }

                            }

                    } else {
                        // Handle failures
                        // ...
                    }
                }


            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.intelligentdream.superimageresolution.R.menu.dashboard_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        if (item != null) {

            if (item.itemId == R.id.menu_log_out) {
                //log out the user
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, getString(R.string.logout_msg), Toast.LENGTH_LONG).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

            if (R.id.menu_settings == item.itemId) {
                //take user to Settings Activity
                startActivity(Intent(this, SettingsActivity::class.java))

            }


        }

        return true
    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        // Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show()

        Snackbar.make(
            Parentdashboard,
            "Click Back again to Exit the app",
            Snackbar.LENGTH_LONG
        ).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
