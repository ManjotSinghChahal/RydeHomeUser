package com.example.rydehomeuser.data.model.getFriendList

data class Body(
    val app_user_id: String,
    val contact: String,
    val created: String,
    val id: String,
    val is_blocked: String,
    val is_user: String,
    val name: String,
    val user_id: String,
    var contactChecked : Boolean = false
)