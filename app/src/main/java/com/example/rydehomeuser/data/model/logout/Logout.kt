package com.example.rydehomeuser.data.model.logout

data class Logout(
    val body: List<Any>,
    val code: Int,
    val message: String,
    val success: Boolean
)