package com.intelligentdream.superimageresolution.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.intelligentdream.superimageresolution.Helpers.getImageUri
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_view_picture.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.sql.DataSource


class ViewPictureActivity : AppCompatActivity() {

    lateinit var orignalLink: String
    lateinit var superLink: String


    override fun onCreate(savedInstanceState: Bundle?) {
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


        btn_share.setOnClickListener {
            sharePostcard(superLink)
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


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }


    fun sharePostcard(url: String) {
        TODO()
        //get resource from url as bitmap
        //startWith(resource )

    }

    private fun startWith(resource: Bitmap) {
        val imageUri = getShareImageUri(this@ViewPictureActivity, resource)


        val msg = "Check my image!"

        Snackbar.make(ParentViewPic, "Sharing...", Snackbar.LENGTH_LONG).show()


        val intent = Intent().apply {
            this.action = Intent.ACTION_SEND
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
            FileProvider.getUriForFile(context, applicationContext.packageName + ".provider", file)
        }
    }

}
