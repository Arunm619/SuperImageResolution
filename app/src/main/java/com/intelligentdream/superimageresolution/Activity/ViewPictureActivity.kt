package com.intelligentdream.superimageresolution.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.intelligentdream.superimageresolution.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_view_picture.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ViewPictureActivity : AppCompatActivity() {

    lateinit var orignalLink: String
    lateinit var superLink: String
    lateinit var target: Target
    val TAG = "Test"


    override fun onCreate(savedInstanceState: Bundle?) {
        val arr = arrayListOf(R.style.AppTheme, R.style.OrangeTheme, R.style.PinkTheme, R.style.GreyTheme)

        val rnd = (0..3).random()
        setTheme(arr[rnd])
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_picture)
        supportActionBar?.title = "Gallery"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)




        if (intent != null) {
            orignalLink =
                intent.getStringExtra(getString(R.string.KEY_ORIGINAL_LINK))
            superLink =
                intent.getStringExtra(getString(R.string.KEY_SUPER_LINK))


            Glide.with(this).load(orignalLink).into(iv_container)


            Snackbar.make(ParentViewPic, "Press the Image to see the changes.", Snackbar.LENGTH_LONG).show()

        } else {
            //Toast.makeText(this, "Error bro", Toast.LENGTH_LONG).show()
            Snackbar.make(ParentViewPic, "Error Please Load Again.", Snackbar.LENGTH_LONG).show()
            finish()
        }



        target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                Snackbar.make(ParentViewPic, "Sharing...", Snackbar.LENGTH_LONG).show()

            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Snackbar.make(ParentViewPic, "Sharing failed", Snackbar.LENGTH_LONG).show()

            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                startWith(bitmap!!)

            }

        }





        iv_container.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // PRESSED

                        //Toast.makeText(baseContext, "Pressed", Toast.LENGTH_SHORT).show()
                        Glide.with(baseContext).load(superLink).into(iv_container)
                        supportActionBar?.title = "Viewing SuperImage"


                    }
                    MotionEvent.ACTION_UP -> {

                        //Toast.makeText(baseContext, "Released", Toast.LENGTH_SHORT).show()
                        // RELEASED
                        Glide.with(baseContext).load(orignalLink).into(iv_container)
                        supportActionBar?.title = "Viewing Original Image"
                    }
                }
                return true
            }
        })

    }

    private fun getImageUri(Context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(Context.contentResolver, inImage, "Title", null);
        return Uri.parse(path)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


    fun sharePostcard(url: String) {


        Picasso.get().load(url).into(target)


    }


    private fun startWith(resource: Bitmap) {
        val imageUri = getShareImageUri(this@ViewPictureActivity, resource)


        val msg = "Check my super image converted by this app!"


        val intent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, msg)
            this.putExtra(Intent.EXTRA_STREAM, imageUri)
            this.type = "image/jpeg"
        }



        startActivity(Intent.createChooser(intent, resources.getText(R.string.share_using)))

    }

    /**
     * Creates a file and gets a Uri for sharing an image
     */
    fun getShareImageUri(context: Context, image: Bitmap): Uri? {

        val path = File(context.cacheDir, "super" + Date().time + ".jpeg")

        Log.d(TAG, "path is $path")

        try {
            val fileOutputStream = FileOutputStream(path)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            fileOutputStream.close()


        } catch (e: IOException) {

            return null
        }

        return getUriForFile(context, path)
    }

    /**
     * Gets a Uri for file
     */
    fun getUriForFile(context: Context, file: File): Uri {


        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {


            Uri.fromFile(file)
        } else {

            FileProvider.getUriForFile(context, "com.intelligentdream.superimageresolution.myprovider", (file))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.viewpictureactivitymeu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)
        if (item != null) {

            if (item.itemId == R.id.menu_share) {
                //share image
                sharePostcard(url = superLink)
            }


        }

        return true
    }

}
