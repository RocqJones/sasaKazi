package com.intoverflown.sasakazi.ui.navdrawer

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.intoverflown.sasakazi.R

class NavDrawer : AppCompatActivity() {
    // Firebase
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    //UI elements
    private var navFullName: TextView? = null
    private var navEmailAddress: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_header_main)

        initREFS()
    }

    private fun initREFS() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        navFullName = findViewById<View>(R.id.nav_fullName) as TextView
        navEmailAddress = findViewById<View>(R.id.nav_emailAddress) as TextView
    }

    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
//
        navEmailAddress!!.text = mUser.email
//        navEmailAddress?.text ?: mUser.email

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                navFullName!!.text = snapshot.child("fullname").value as String
//                navFullName?.text ?: snapshot.child("fullname").value as String
            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }
}
