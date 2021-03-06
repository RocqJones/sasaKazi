package com.intoverflown.sasakazi.ui.navdrawer

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.icu.number.NumberRangeFormatter.with
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.settings.EditProfileActivity


class ProfileActivity : AppCompatActivity() {

    // Firebase
    private var mAuth : FirebaseAuth? = null
    private var mDatabase : FirebaseDatabase? = null
    private var mDatabaseReference : DatabaseReference? = null

    // Ui elements
    private var profileFullName : TextView? = null
    private var profilePhoneNum : TextView? = null
    private var profilePicture : ImageView? = null
    private var profileIcon: ImageView? = null
    private var profileBtn: Button? = null
    private var profileBio: TextView? = null

    // image vars
    private val IMAGE_REQUEST_CODE = 22
    private var imageUri: Uri? = null
    private var storageRef: StorageReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeRefs()
    }

    private fun initializeRefs() {
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()

        // create user img db
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        profileFullName = findViewById<View>(R.id.user_profileFullName) as TextView
        profilePhoneNum = findViewById<View>(R.id.user_profilePhoneNo) as TextView
        profilePicture = findViewById<View>(R.id.user_profilePicture) as ImageView
        profileIcon = findViewById<View>(R.id.user_imgIC) as ImageView
        profileBtn = findViewById<View>(R.id.user_profileBtn) as Button
        profileBio = findViewById<View>(R.id.user_bioInfo) as TextView

        profileBtn!!.setOnClickListener { intentToEditProfile() }
        profileIcon!!.setOnClickListener { selectImg() }
        profilePicture!!.setOnClickListener { selectImg() }
    }

    private fun intentToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun selectImg() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image..."), IMAGE_REQUEST_CODE)
    }

    // override onActivityResult fun which gets the image data
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data!!.data != null) {
            imageUri = data.data
            Toast.makeText(this, "uploading...", Toast.LENGTH_LONG).show()
            uploadImgToDatabase()
        }
    }

    private fun uploadImgToDatabase() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("Uploading image, please wait...")
        progressBar.show()

        if (imageUri != null) {
            // make user id unique such that if another user names the image the same way it won't replace it
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")

            val uploadTask : StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (task.isSuccessful) {
                    task.exception?.let { throw  it }
                }
                return@Continuation fileRef.downloadUrl

                // now store the url in the Real-Time database by adding a listener
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth!!.currentUser!!.uid
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    val currentUsersDb = mDatabaseReference!!.child(userId)

                    // check if db has photo url
                    currentUsersDb.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild("profileurl")) {
                                // update clause
                                val map_profile_url = HashMap<String, Any>()
                                map_profile_url["profileurl"] = url
                                currentUsersDb.updateChildren(map_profile_url)
                            } else {
                                // doesn't exist
                                currentUsersDb.child("profileurl").setValue(url)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {  }
                    })
                    progressBar.dismiss()
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Picasso.get().load(snapshot.child("profileurl").value as String).into(profilePicture)
                profilePicture?.let {
                    Glide.with(this@ProfileActivity).load(snapshot.child("profileurl").value as String).circleCrop().into(
                        it
                    )
                }
                profileFullName!!.text = snapshot.child("fullname").value as String
                profilePhoneNum!!.text = snapshot.child("phone").value as String

                // check if user has updated bio or not
                if (snapshot.hasChild("bio")) {
                    profileBio!!.text = snapshot.child("bio").value as String
                    profileBio!!.visibility = View.VISIBLE
                } else {
                    profileBio!!.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}