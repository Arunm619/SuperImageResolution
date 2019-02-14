package com.intelligentdream.superimageresolution

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.intelligentdream.superimageresolution.Helpers.verifyAvailableNetwork

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!verifyAvailableNetwork(this)) {
            Toast.makeText(this, "Network is not available Please connect to Internet", Toast.LENGTH_LONG).show()
        }
    }
}
