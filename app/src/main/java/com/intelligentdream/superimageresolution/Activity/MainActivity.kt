package com.intelligentdream.superimageresolution.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.intelligentdream.superimageresolution.Helpers.verifyAvailableNetwork
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var user: FirebaseUser? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //checking internet Connection
        if (!verifyAvailableNetwork(this)) {
            Toast.makeText(this, "Network is not available Please connect to Internet", Toast.LENGTH_LONG).show()
        }



        setOnclickListeners()

        FirebaseApp.initializeApp(this)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->

            user = firebaseAuth.currentUser

            if (user != null) {
                val intent = Intent(this, DashBoardActivity::class.java)

                startActivity(intent)
                //   Toast.makeText(this, "Welcome User - ${user?.uid}", Toast.LENGTH_LONG).show()

                finish()


            } else {
                Snackbar.make(ParentMain, "Please Authenticate", Snackbar.LENGTH_LONG).show()

                //Toast.makeText(this, "Please Sign in", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setOnclickListeners() {
        btn_main_signup.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()

        }

        btn_main_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


    override fun onStart() {
        super.onStart()
        //   if(mAuth!=null)
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuth != null)
            mAuth!!.removeAuthStateListener(mAuthListener!!)
    }
}
