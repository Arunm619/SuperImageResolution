package com.intelligentdream.superimageresolution.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.intelligentdream.superimageresolution.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar!!.title = getString(R.string.createAnaccount)

        mAuth = FirebaseAuth.getInstance()


        btn_sign_up_submit.setOnClickListener {


            val email = et_sign_up_user_email.text.toString()
            val userName = et_signup_user_name.text.toString()
            val password = et_sign_up_password1.text.toString()
            val confirmPass = et_sign_up_password2.text.toString()


            if (userName.trim().equals("")) {
                Snackbar.make(rl_signupActivity, "Enter User Name", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (email.trim().equals("")) {
                Snackbar.make(rl_signupActivity, "Enter Email address", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }


            if (password.trim().equals("")) {
                Snackbar.make(rl_signupActivity, "Enter Password", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Snackbar.make(
                    rl_signupActivity,
                    "Password should be of at least 6 characters length",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }



            if (confirmPass.trim() != password) {
                Snackbar.make(rl_signupActivity, "Passwords don't match", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }


            createAccount(userName, email, password)


        }

    }


    fun createAccount(userName: String, userEmail: String, password: String) {
        mAuth!!.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {
                    //    Toast.makeText(this,"Sucess",Toast.LENGTH_LONG).show()
                    val currentUser = mAuth?.currentUser
                    val userId = currentUser!!.uid


                    /*   val userSettings = UserSettings(userName, "", "")

                       userSettings.Image = "null"
                       userSettings.Status = "Hey there I am using Messaing App"
                       userSettings.ThumbNail = "null"

                       Toast.makeText(this, userId, Toast.LENGTH_LONG).show()
                       mDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                       mDatabase!!.setValue(userSettings).addOnCompleteListener { task: Task<Void> ->
                           if (task.isSuccessful) {

                               Toast.makeText(this, "Vardhu vardhu", Toast.LENGTH_LONG).show()

                               Snackbar.make(rl_signupActivity, "User Created SUccessfull", Snackbar.LENGTH_LONG).show()

                           } else {

                               Toast.makeText(
                                   this,
                                   "try again bro ${task.exception.toString().split(":")[0]}",
                                   Toast.LENGTH_LONG
                               ).show()

                               Snackbar.make(rl_signupActivity, task.exception.toString(), Snackbar.LENGTH_LONG).show()

                           }

                       }
   */

                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra(getString(R.string.KEY_DISPLAY_NAME), userName)
                    startActivity(intent)
                    finish()
                } else {

//                    Toast.makeText(this, "try again bro ${task.exception.toString().split(":")[0]}", Toast.LENGTH_LONG)
                    //                      .show()
                    Snackbar.make(rl_signupActivity, task.exception.toString().split(":")[1], Snackbar.LENGTH_LONG)
                        .show()

                }
            }

    }


    override fun onBackPressed() {
        startActivity(Intent(baseContext, MainActivity::class.java))
        finish()
    }
}
