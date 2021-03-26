package com.intoverflown.sasakazi.ui.discussions

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.intoverflown.sasakazi.R
import com.intoverflown.sasakazi.databinding.ChatMobileBinding
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MessageViewHolder
import com.intoverflown.sasakazi.ui.discussions.reusableutils.Messages
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MyButtonObserver
import com.intoverflown.sasakazi.ui.discussions.reusableutils.MyScrollToBottomObserver

class ChatMobile : AppCompatActivity() {

    private val TAG = "ChatMobile"

    val MESSAGES_CHILD = "messages-mobile"
    val ANONYMOUS = "Anonymous"
    var sName: String? = null
    var usrProfileUrl: String? = null

    private val REQUEST_IMAGE = 2
    private val LOADING_IMAGE_URL = "https://www.google.com/images/spin-32.gif"

    private var mBinding: ChatMobileBinding? = null

    // 1. Firebase instance variables
    private var mFirebaseAuth: FirebaseAuth? = null

    // initializing the Firebase Realtime Database and adding a listener to handle changes made to the data
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mFirebaseAdapter: FirebaseRecyclerAdapter<Messages?, MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use View Binding
        mBinding = ChatMobileBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

        // 2. Initialize Firebase Auth and check if the user is signed in
        mFirebaseAuth = FirebaseAuth.getInstance()
        var mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.stackFromEnd = true
        mBinding!!.recyclerView.layoutManager = mLinearLayoutManager

        // Initialize Realtime Database
        mDatabase = FirebaseDatabase.getInstance()
        val messagesRef = mDatabase!!.reference.child(MESSAGES_CHILD)
        mDatabaseReference = mDatabase!!.reference.child("Users")

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options =
            FirebaseRecyclerOptions.Builder<Messages>()
                .setQuery(
                    messagesRef,
                    Messages::class.java
                )
                .build()
        mFirebaseAdapter = object : FirebaseRecyclerAdapter<Messages?, MessageViewHolder>(
            options
        ) {
            override fun onCreateViewHolder(
                viewGroup: ViewGroup,
                i: Int
            ): MessageViewHolder {
                val inflater = LayoutInflater.from(viewGroup.context)
                return MessageViewHolder(
                    inflater.inflate(R.layout.item_message, viewGroup, false)
                )
            }

            override fun onBindViewHolder(
                vh: MessageViewHolder,
                position: Int,
                message: Messages
            ) {
                mBinding!!.progressBar.visibility = ProgressBar.INVISIBLE
                vh.bindMessage(message)
            }
        }
        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager.stackFromEnd = true
        mBinding!!.recyclerView.layoutManager = mLinearLayoutManager
        mBinding!!.recyclerView.adapter = mFirebaseAdapter

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver.java for details
        mFirebaseAdapter!!.registerAdapterDataObserver(
            MyScrollToBottomObserver(
                mBinding!!.recyclerView,
                mFirebaseAdapter!!,
                mLinearLayoutManager
            )
        )
        mBinding!!.inputMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mBinding!!.sendMessageBtn.isEnabled =
                    charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        // Disable the send button when there's no text in the input field
        // See MyButtonObserver.java for details
        mBinding!!.inputMessage.addTextChangedListener(
            MyButtonObserver(
                mBinding!!.sendMessageBtn
            )
        )

        // When the send button is clicked, send a text message
        mBinding!!.sendMessageBtn.setOnClickListener { // Send messages on click.
            val messages =
                Messages(
                    mBinding!!.inputMessage.text.toString(),
                    getUserName(),
                    getUserPhotoUrl(),
                    null /* no image */
                )
            mDatabase!!.reference.child(MESSAGES_CHILD).push().setValue(messages)
            mBinding!!.inputMessage.setText("")
        }

        // When the image button is clicked, launch the image picker
        mBinding!!.addMessageImageView.setOnClickListener { // Select image for image message on click.
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }
    }

    // Once the user has selected an image, a call to the MainActivity's onActivityResult will be fired.
    // This is where you handle the user's image selection.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                val uri = data.data
                Log.d(TAG, "Uri: " + uri.toString())
                val user = mFirebaseAuth!!.currentUser
                val tempMessage =
                    Messages(
                        null, getUserName(), getUserPhotoUrl(), LOADING_IMAGE_URL
                    )
                mDatabase!!.reference.child(MESSAGES_CHILD).push()
                    .setValue(tempMessage,
                        DatabaseReference.CompletionListener { databaseError, databaseReference ->
                            if (databaseError != null) {
                                Log.w(
                                    TAG, "Unable to write message to database.",
                                    databaseError.toException()
                                )
                                return@CompletionListener
                            }

                            // Build a StorageReference and then upload the file
                            val key = databaseReference.key
                            val storageReference = FirebaseStorage.getInstance()
                                .getReference(user.uid)
                                .child(key!!)
                                .child(uri!!.lastPathSegment!!)
                            putImageInStorage(storageReference, uri, key)
                        })
            }
        }
    }

    // Once the upload is complete you will update the message to use the appropriate image.
    private fun putImageInStorage(storageReference: StorageReference, uri: Uri?, key: String?) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri!!)
            .addOnSuccessListener(
                this
            ) { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val messages =
                            Messages(
                                null, getUserName(), getUserPhotoUrl(), uri.toString()
                            )
                        mDatabase!!.reference
                            .child(MESSAGES_CHILD)
                            .child(key!!)
                            .setValue(messages)
                    }
            }
            .addOnFailureListener(
                this
            ) { e -> Log.w(TAG, "Image upload task was not successful.", e) }
    }

    override fun onPause() {
        mFirebaseAdapter!!.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onStart() {
        super.onStart()

        // store name of the user in a String var
        val mUserId = mFirebaseAuth!!.currentUser.uid
        mDatabaseReference!!.child(mUserId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sName = snapshot.child("fullname").value.toString()
                usrProfileUrl = snapshot.child("profileurl").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // implement the getUserPhotoUrl() amd getUserName() methods
    private fun getUserPhotoUrl(): String? {
        val user = mFirebaseAuth!!.currentUser.uid
        return if (user != null) {
            usrProfileUrl
        } else java.lang.String.valueOf(R.drawable.baseline_account_circle_black_36dp)
    }

    private fun getUserName(): String? {
        // if current user exists, set the name of the sender
        val mUserId = mFirebaseAuth!!.currentUser.uid
        return if (mUserId != null) {
            sName
        } else ANONYMOUS
    }
}