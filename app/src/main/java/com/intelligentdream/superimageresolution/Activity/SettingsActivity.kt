package com.intelligentdream.superimageresolution.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.intelligentdream.superimageresolution.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.title = getString(R.string.settings)

    }
}
