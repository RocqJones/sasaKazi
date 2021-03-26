package com.intoverflown.sasakazi.ui.discussion_migrations.reusableutils

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.intoverflown.sasakazi.R

class MessageViewHolder(v: View?) : RecyclerView.ViewHolder(v!!) {
    var messageTextView: TextView
    var messageImageView: ImageView
    var messengerTextView: TextView
    var messengerImageView: ImageView
    fun bindMessage(messages: Messages) {
        if (messages.text != null) {
            messageTextView.text = messages.text
            messengerTextView.text = messages.fullname
            messageTextView.visibility = TextView.VISIBLE
            messageImageView.visibility = ImageView.GONE
            Glide.with(messengerImageView.context).load(messages.photoUrl).circleCrop()
                .into(messengerImageView)
        } else if (messages.imageUrl != null) {
            val imageUrl = messages.imageUrl
            if (imageUrl!!.startsWith("gs://")) {
                val storageReference = FirebaseStorage.getInstance()
                    .getReferenceFromUrl(imageUrl)
                storageReference.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        Glide.with(messageImageView.context)
                            .load(downloadUrl)
                            .into(messageImageView)
                    }
                    .addOnFailureListener { e ->
                        Log.w(
                            TAG,
                            "Getting download url was not successful.",
                            e
                        )
                    }
            } else {
                Glide.with(messageImageView.context)
                    .load(messages.imageUrl)
                    .into(messageImageView)

                // show sender profile picture here
                Glide.with(messengerImageView.context).load(messages.photoUrl).circleCrop()
                    .into(messengerImageView)
            }
            messageImageView.visibility = ImageView.VISIBLE
            messageTextView.visibility = TextView.GONE
        }
    }

    companion object {
        private const val TAG = "MessageViewHolder"
    }

    init {
        messageTextView = itemView.findViewById<View>(R.id.messageTextView) as TextView
        messageImageView = itemView.findViewById<View>(R.id.messageImageView) as ImageView
        messengerTextView = itemView.findViewById<View>(R.id.messengerTextView) as TextView
        messengerImageView = itemView.findViewById<View>(R.id.messengerImageView) as ImageView
    }
}