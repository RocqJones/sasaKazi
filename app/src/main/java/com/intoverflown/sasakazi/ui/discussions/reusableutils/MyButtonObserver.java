package com.intoverflown.sasakazi.ui.discussions.reusableutils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;

public class MyButtonObserver implements TextWatcher {

    private final ImageButton mButton;

    public MyButtonObserver(ImageButton button) {
        this.mButton = button;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mButton.setEnabled(charSequence.toString().trim().length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

}
