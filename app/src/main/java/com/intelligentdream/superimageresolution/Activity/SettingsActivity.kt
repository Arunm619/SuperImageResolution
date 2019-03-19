package com.intelligentdream.superimageresolution.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val arr = arrayListOf(R.style.AppTheme, R.style.OrangeTheme, R.style.PinkTheme, R.style.GreyTheme)

        val rnd = (0..3).random()
        setTheme(arr[rnd])
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        switch_darkmode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {


                setContentView(R.layout.activity_settings)
            } else {
                setContentView(R.layout.activity_settings)
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()

    }

}
