package com.example.rydehomeuser.data.model.editAccount

data class UpdateProfile(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)