package com.example.examresultenteringsystem

class User {
    var username: String? = null
    var ic: String? = null
    var email: String? = null
    var uid: String? = null
    var position: String? = null


    constructor(){}

    constructor(username: String?, ic:String?, email:String?, uid:String?,position:String?){
        this.username = username
        this.ic = ic
        this.email = email
        this.uid = uid
        this.position = position
    }
}