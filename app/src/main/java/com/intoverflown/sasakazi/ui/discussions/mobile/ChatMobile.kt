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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.chat_mobile)

        mProgressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        mInputMessage = findViewById<View>(R.id.inputMessage) as EditText
        mSendMessageBtn = findViewById<View>(R.id.sendMessageBtn) as ImageButton
    }
}