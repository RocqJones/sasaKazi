package com.intoverflown.sasakazi.ui.contacts

class Users {
    var id: String? = null
    var fullname: String? = null
    var phone : String? = null
    var profileurl : String? = null

    constructor() {}

    constructor(id: String?, fullname: String?, phone: String?, profileurl: String) {
        this.id = id
        this.fullname = fullname
        this.phone = phone
        this.profileurl = profileurl
    }
}