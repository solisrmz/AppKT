package com.example.songapp

class Remember {
    companion object Factory {
        fun create(): Remember = Remember()
    }

    var objectId: String? = null
    var taskDesc: String? = null
    var taskAut: String? = null
    var done: Boolean? = false
}