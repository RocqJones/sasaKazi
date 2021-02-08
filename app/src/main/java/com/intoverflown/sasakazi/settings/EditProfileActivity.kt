package com.intoverflown.sasakazi.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.intoverflown.sasakazi.R

class EditProfileActivity : AppCompatActivity() {

    // Firebase
    private var mAuth : FirebaseAuth? = null
    private var mDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null

    // ui elements
    private var update_fullname : EditText? = null
    private var update_phoneno : EditText? = null
    private var update_addBio : EditText? = null
    private var update_btn : Button? = null

    // Global variables
    private var str_updatefullname: String? = null
    private var str_updatephoneno: String? = null
    private var str_updateaddbio: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        callREFSHere()
    }

    private fun callREFSHere() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        update_fullname = findViewById<View>(R.id.updateProfileName) as EditText
        update_phoneno = findViewById<View>(R.id.updatePhoneNumber) as EditText
        update_addBio = findViewById<View>(R.id.updateAddBio) as EditText
        update_btn = findViewById<View>(R.id.updateBtn) as Button

        update_btn!!.setOnClickListener { updateCurrentUserDb() }
    }

    private fun updateCurrentUserDb() {
        str_updatefullname = update_fullname?.text.toString()
        str_updatephoneno = update_phoneno?.text.toString()
        str_updateaddbio = update_addBio?.text.toString()

        val userId = mAuth!!.currentUser!!.uid
        val currentUsersDb = mDatabaseReference!!.child(userId)

        if (!TextUtils.isEmpty(str_updatefullname) && !TextUtils.isEmpty(str_updatephoneno) && !TextUtils.isEmpty(str_updateaddbio)) {
            currentUsersDb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("fullname") && snapshot.hasChild("phone") && snapshot.hasChild("bio")) {
                        val map_fullname = HashMap<String, Any>()
                        val map_phoneNo = HashMap<String, Any>()
                        val map_bio = HashMap<String, Any>()

                        map_fullname["fullname"] = str_updatefullname!!
                        map_phoneNo["phone"] = str_updatephoneno!!
                        map_bio["bio"] = str_updateaddbio!!

                        currentUsersDb.updateChildren(map_fullname)
                        currentUsersDb.updateChildren(map_phoneNo)
                        currentUsersDb.updateChildren(map_bio)
                    } else {
                        currentUsersDb.child("bio").setValue(str_updateaddbio)
                    }
                }

                override fun onCancelled(error: DatabaseError) {  }
            })
        }
    }

    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // update_fullname!!.text = snapshot.child("fullname").value as Editable
                update_fullname!!.text = Editable.Factory.getInstance().newEditable(snapshot.child("fullname").value as String)
                update_phoneno!!.text = Editable.Factory.getInstance().newEditable(snapshot.child("phone").value as String)
                if (snapshot.hasChild("bio")) {
                    update_addBio!!.text = Editable.Factory.getInstance().newEditable(snapshot.child("bio").value as String)
                } else {
                    update_addBio!!.text = null
                }
            }

            override fun onCancelled(error: DatabaseError) {  }
        })
    }
}