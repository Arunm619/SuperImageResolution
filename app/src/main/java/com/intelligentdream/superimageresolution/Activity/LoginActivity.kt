package com.intelligentdream.superimageresolution.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "Login with your account"



        mAuth = FirebaseAuth.getInstance()


        btn_login_submit.setOnClickListener {
            val email = et_login_mail.text.toString()
            val password = et_login_password.text.toString()


            if (email.trim().equals("")) {
                Snackbar.make(rl_LoginActivity, "Enter Email", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.trim().equals("")) {
                Snackbar.make(rl_LoginActivity, "Enter Password", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Snackbar.make(
                    rl_LoginActivity,
                    "Password length has to be minimum of six(6) characters long",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            logIn(email, password)
        }

    }

    private fun logIn(email: String, password: String) {

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    val userName = email.split("@")[0]

                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra(getString(R.string.KEY_DISPLAY_NAME), userName)
                    startActivity(intent)
                    finish()

                } else {
                    Snackbar.make(
                        rl_LoginActivity, task.exception.toString().split(":")[1],
                        Snackbar.LENGTH_LONG
                    ).show()

                }
            }

    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext,MainActivity::class.java))
        finish()
    }
}