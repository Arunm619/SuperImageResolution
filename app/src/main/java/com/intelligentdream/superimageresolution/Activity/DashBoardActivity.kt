package com.intelligentdream.superimageresolution.Activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_dash_board.*
import java.util.jar.Manifest
import com.intelligentdream.superimageresolution.Helpers.*

class DashBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        supportActionBar!!.title = "Dashboard"


        btn_choose_pic.setOnClickListener {
            pickImageFromGalley()
        }


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
                    Toast.makeText(this, "FAILED PLEASE IT REQUIED", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            Toast.makeText(this, data?.data.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
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


}
