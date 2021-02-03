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
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.intoverflown.sasakazi.MainActivity
import com.intoverflown.sasakazi.R
import org.w3c.dom.Text

class SignupActivity : AppCompatActivity() {
    // UI elements
    private var fullname: EditText? = null
    private var email: EditText? = null
    private var phone: EditText? = null
    private var password: EditText? = null
    private var confirm_password: EditText? = null
    private var createaccount_btn: Button? = null

    private var mProgressBar: ProgressDialog? = null

    //Firebase preferences
    private var mDatabaseReferences: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    // Mapping errors with the originating activity for debugging purpose
    private var TAG = "SignupActivity"

    // Global variables
    private var str_fullname: String? = null
    private var str_email: String? = null
    private var str_phone: String? = null
    private var str_password: String? = null
    private var str_confirmpwd: String? = null

    // default user profile avatar
    private val url = "https://firebasestorage.googleapis.com/v0/b/sasakazi.appspot.com/o/User%20Images%2Favatar.png?alt=media&token=b4170626-4421-4ffc-adfb-667a0583a4a5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // ui references
        initializeUiReferences()
    }

    private fun initializeUiReferences() {
        // from layout
        fullname = findViewById<View>(R.id.fullname_edittext) as EditText
        email = findViewById<View>(R.id.email_edittext) as EditText
        phone = findViewById<View>(R.id.phone_edittext) as EditText
        password = findViewById<View>(R.id.password_edittext) as EditText
        confirm_password = findViewById<View>(R.id.confirmpassword_edittext) as EditText
        createaccount_btn = findViewById<View>(R.id.create_account_button) as Button

        mProgressBar = ProgressDialog(this)

        // Firebase here
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReferences = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        // btn action onClick
        createaccount_btn!!.setOnClickListener{ createNewAccount() }
    }

    private fun createNewAccount() {
        // first get user string values from EditText
        str_fullname = fullname?.text.toString()
        str_email = email?.text.toString()
        str_phone = phone?.text.toString()
        str_password = password?.text.toString()
        str_confirmpwd = confirm_password?.text.toString()

        // Then, we will validate this text and show appropriate error message if any value is empty
        if (!TextUtils.isEmpty(str_fullname) && !TextUtils.isEmpty(str_email) && !TextUtils.isEmpty(str_phone)
                && !TextUtils.isEmpty(str_password) && !TextUtils.isEmpty(str_confirmpwd) && (str_password == str_confirmpwd)) {
            // let's conn to firebase and since it might take time show progress bar here...
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            // using addOnCompleteListener, we get to know if the task was successful or not
            mAuth!!.createUserWithEmailAndPassword(str_email!!, str_password!!).addOnCompleteListener(this) {task ->
                mProgressBar!!.hide()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail: success")
                    val userId = mAuth!!.currentUser!!.uid

                    // verify via email
                    verifyWithEmail()

                    // updater user's profile information to the real-time database
                    val currentUsersDb = mDatabaseReferences!!.child(userId)
                    currentUsersDb.child("fullname").setValue(str_fullname)
                    currentUsersDb.child("phone").setValue(str_phone)
                    currentUsersDb.child("profile-url").setValue(url)

                    // then start a new activity once user is created
                    sendUserToHomePage()
                } else {
                    // if signing fails, display message to user
                    Log.w(TAG, "createUserWithEmail: failed", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun verifyWithEmail() {
        // method to verify user’s email address
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "" + "Verification email sent to " + mUser.email, Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(this, "Failed to send verification", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserToHomePage() {
        // "FLAG_ACTIVITY_CLEAR_TOP" - flag clears the current activity from stack then instead of launching a new instance of that activity,
        // all of the other activities on top of it will be closed and this Intent will be delivered to the (now on top)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    // send to login activity of they have account
    fun intentToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}