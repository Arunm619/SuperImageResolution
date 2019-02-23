package com.intelligentdream.superimageresolution.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_view_picture.*


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
}
