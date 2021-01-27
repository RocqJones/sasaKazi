package com.intoverflown.sasakazi.ui.navdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.intoverflown.sasakazi.R

class ProfileActivity : AppCompatActivity() {

    // Firebase
    private var mAuth : FirebaseAuth? = null
    private var mDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null

    // Ui elements
    private var profileFullName : TextView? = null
    private var profilePhoneNum : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeRefs()
    }

    private fun initializeRefs() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        profileFullName = findViewById<View>(R.id.user_profileFullName) as TextView
        profilePhoneNum = findViewById<View>(R.id.user_profilePhoneNo) as TextView
    }

    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                profileFullName!!.text = snapshot.child("fullname").value as String
                profilePhoneNum!!.text = snapshot.child("phone").value as String
            }

            override fun onCancelled(error: DatabaseError) {  }
        })
    }
}