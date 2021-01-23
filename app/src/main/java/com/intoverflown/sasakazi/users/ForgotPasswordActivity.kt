package com.intoverflown.sasakazi.users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.intoverflown.sasakazi.R

class ForgotPasswordActivity : AppCompatActivity() {

    private  val TAG = "ForgotPasswordActivity"

    // UI elements
    private var user_email: EditText? = null
    private var request_pwd_btn: Button? = null

    // Firebase ref
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // initialization
        initializeReferences()
    }

    private fun initializeReferences() {
        user_email = findViewById<View>(R.id.forgotpwd_Email_EditTxt) as EditText
        request_pwd_btn = findViewById<View>(R.id.forgotpwd_button) as Button

        mAuth = FirebaseAuth.getInstance()

        request_pwd_btn!!.setOnClickListener { sendPWDResetEmail() }
    }

    private fun sendPWDResetEmail() {
        val str_email = user_email?.text.toString()

        if (!TextUtils.isEmpty(str_email)) {
            mAuth!!.sendPasswordResetEmail(str_email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val message = "Email sent"
                    Log.d(TAG, message)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    intentBackToLoginScreen()
                } else {
                    val message = "No user found with this email"
                    task.exception!!.message?.let { Log.w(TAG, it) }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            val message = "Enter Email"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun intentBackToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}