package com.example.examresultenteringsystem

class Student {
    var studentname: String? = null
    var classname: String? = null
    var test1: String? = null
    var test2: String? = null
    var status:String? = null


    constructor(){}

    constructor(studentname: String?, classname:String?, test1:String?, test2:String?,status:String?){
        this.studentname= studentname
        this.classname = classname
        this.test1 = test1
        this.test2 = test2
        this.status = status
    }
}