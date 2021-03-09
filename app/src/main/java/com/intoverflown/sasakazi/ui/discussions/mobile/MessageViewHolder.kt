package com.intoverflown.sasakazi.ui.discussions.mobile


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intoverflown.sasakazi.R


class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val TAG = "MessageViewHolder"

    var messageReceiverTextView: TextView? = null
    var messageSenderTextView: TextView? = null

    fun MessageViewHolder(v: View?) {
        super.itemView
        messageReceiverTextView = itemView.findViewById<View>(R.id.textViewReceiver) as TextView
        messageSenderTextView = itemView.findViewById<View>(R.id.textViewSender) as TextView
    }

    fun bindMessage(mobileMessages: MobMessages) {
        if (mobileMessages.getText() != null) {
            messageReceiverTextView!!.setText(mobileMessages.getText())
            messageSenderTextView!!.setText(mobileMessages.getName())
            messageReceiverTextView!!.setVisibility(TextView.VISIBLE)
        }
    }
}