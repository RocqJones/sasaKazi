package com.intoverflown.sasakazi.ui.discussions.mobile

class Messages {
    private var text: String? = null
    private var name: String? = null

    constructor(text: String?, name: String?) {
        this.text = text
        this.name = name
    }

    fun getText(): String? {
        return text
    }

    fun setText(text: String?) {
        this.text = text
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }
}
