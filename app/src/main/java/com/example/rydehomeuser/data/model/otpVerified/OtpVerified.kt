package com.example.rydehomeuser.data.model.otpVerified

data class OtpVerified(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)