package com.intoverflown.sasakazi.ui.discussions.mobile

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

class SendMsgBtnObserver(private val mButton: Button) : TextWatcher {
    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        mButton.isEnabled = charSequence.toString().trim { it <= ' ' }.isNotEmpty()
    }

    override fun afterTextChanged(editable: Editable) {}
}