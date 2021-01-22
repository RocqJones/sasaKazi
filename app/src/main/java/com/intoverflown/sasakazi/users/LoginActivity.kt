package com.intoverflown.sasakazi.users

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.intoverflown.sasakazi.MainActivity
import com.intoverflown.sasakazi.R

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    // global variables
    private var str_email: String? = null
    private var str_password: String? = null
    private var mProgressBar: ProgressDialog? = null

    //UI elements
    private var email: EditText? = null
    private var password: EditText? = null
    private var login_btn: Button? = null
    private var forgot_pwd: TextView? = null

    // Firebase
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialization
        initializeReferences()
    }

    private fun initializeReferences() {
        email = findViewById<View>(R.id.email_edittext) as EditText
        password = findViewById<View>(R.id.password_edittext) as EditText
        login_btn = findViewById<View>(R.id.login_button) as Button
        forgot_pwd = findViewById<View>(R.id.forgot_password) as TextView

        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        forgot_pwd!!.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        login_btn!!.setOnClickListener{ loginUser() }
    }

    private fun loginUser() {
        str_email = email?.text.toString()
        str_password = password?.text.toString()

        if (!TextUtils.isEmpty(str_email) && !TextUtils.isEmpty(str_password)) {
            mProgressBar!!.setMessage("Signing in user...")
            mProgressBar!!.show()
            Log.d(TAG, "Logging in user...")

            mAuth!!.signInWithEmailAndPassword(str_email!!, str_password!!).addOnCompleteListener(this) { task ->
                mProgressBar!!.hide()
                if (task.isSuccessful) {
                    // sign in success and intent to homepage
                    Log.d(TAG,"signInWithEmail:success")
                    sendUserToHomePage()
                } else {
                    // if login fails, display message
                    Log.e(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                    // Show forgot password TextView here
                    forgot_pwd!!.visibility = View.VISIBLE
                }
            }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendUserToHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    // Check if the user is signed in, if yes take them to where they left off unless they logged out
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = mAuth?.currentUser
        if (currentUser != null) {
            sendUserToHomePage()
        } else {
            loginUser()
        }
    }

    // send user to signup activity if they don't have account
    fun intentToSignup(view: View) {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }
}