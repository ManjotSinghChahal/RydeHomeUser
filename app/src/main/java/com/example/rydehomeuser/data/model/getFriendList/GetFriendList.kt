package com.example.rydehomeuser.data.model.getFriendList

data class GetFriendList(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)