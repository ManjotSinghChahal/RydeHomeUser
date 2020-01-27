package com.example.rydehomeuser.data.model.sendOtp

data class SendOtp(
    val body: Int,
    val code: Int,
    val message: String,
    val success: Boolean
)