package com.intoverflown.sasakazi.ui.discussions.mobile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.intoverflown.sasakazi.R


class ChatMobile : AppCompatActivity() {

    // UI
    private var mProgressBar : ProgressBar? = null
    private var mInputMessage : EditText? = null
    private var mSendMessageBtn : ImageButton? = null

    private val TAG = "ChatMobileActivity"
    private val MESSAGES_CHILD = "mobile-messages"
    private val ANONYMOUS: String = "Anonymous"

//    private var mBinding: ChatMobileBinding? = null
    private var mLinearLayoutManager: LinearLayoutManager? = null
    private var messageRecyclerView : RecyclerView? = null

    // 1. Firebase instance variables
    private val mFirebaseAuth: FirebaseAuth? = null
    private var mDatabaseReference : DatabaseReference? = null

    // nitializing the Firebase Realtime Database and adding a listener to handle changes made to the data
    private var mDatabase: FirebaseDatabase? = null
    private var mFirebaseAdapter: FirebaseRecyclerAdapter<MobMessages, MessageViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.chat_mobile)

        mProgressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        mInputMessage = findViewById<View>(R.id.inputMessage) as EditText
        mSendMessageBtn = findViewById<View>(R.id.sendMessageBtn) as ImageButton

        // We now us View Binding
//        mBinding = ChatMobileBinding.inflate(layoutInflater)
//        setContentView(mBinding!!.root)

        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        messageRecyclerView?.layoutManager = mLinearLayoutManager

        // Initialize Realtime Database
        mDatabase = FirebaseDatabase.getInstance()
        val messagesRef = mDatabase!!.reference.child(MESSAGES_CHILD)

        // The FirebaseRecyclerAdapter class comes from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android
        val options: FirebaseRecyclerOptions<MobMessages> = FirebaseRecyclerOptions.Builder<MobMessages>()
                .setQuery(messagesRef, MobMessages::class.java)
                .build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<MobMessages, MessageViewHolder>(options) {
            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MessageViewHolder {
                val inflater = LayoutInflater.from(viewGroup.context)
                return MessageViewHolder(inflater.inflate(R.layout.content_sender_receiver_bg, viewGroup, false))
            }

            override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: MobMessages) {
                mProgressBar!!.visibility = ProgressBar.INVISIBLE
                holder.bindMessage(model)
            }
        }

        mLinearLayoutManager = LinearLayoutManager(this)
        mLinearLayoutManager!!.stackFromEnd = true
        messageRecyclerView?.layoutManager = mLinearLayoutManager
        messageRecyclerView?.adapter = mFirebaseAdapter

        // Scroll down when a new message arrives
        // See ScrollToBottomObserver.kt for details
        messageRecyclerView?.let {
            ScrollToBottomObserver(it,
                    mFirebaseAdapter as FirebaseRecyclerAdapter<MobMessages, MessageViewHolder>, mLinearLayoutManager!!)
        }?.let {
            (mFirebaseAdapter as FirebaseRecyclerAdapter<MobMessages, MessageViewHolder>).registerAdapterDataObserver(
                    it)
        }

        mInputMessage!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                mSendMessageBtn!!.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        })

        // Disable the send button when there's no text in the input field. See SendMsgBtnObserver.kt for details
        mInputMessage!!.addTextChangedListener(SendMsgBtnObserver(mSendMessageBtn))

        // When the send button is clicked, send a text message
        mSendMessageBtn!!.setOnClickListener { // Send messages on click.
            val myMessage = MobMessages(mInputMessage!!.text.toString(), getUserName())
            mDatabase!!.reference.child(MESSAGES_CHILD).push().setValue(myMessage)
            mInputMessage!!.setText("")
        }
    }

    private fun getUserName(): String {
        mDatabaseReference = mDatabase!!.reference.child("Users")
//        profileFullName!!.text = snapshot.child("fullname").value as String

        val mUser = mFirebaseAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        return mUserReference.child("fullname").toString()

//        val user = mFirebaseAuth.currentUser
//        return user?.displayName?.toString() ?: ANONYMOUS
    }

    override fun onPause() {
        mFirebaseAdapter!!.stopListening()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAdapter!!.startListening()
    }
}