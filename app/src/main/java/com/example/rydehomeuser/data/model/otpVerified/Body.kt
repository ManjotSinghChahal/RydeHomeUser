package com.example.rydehomeuser.data.model.otpVerified

data class Body(
    val authorization_key: String,
    val birthday: String,
    val business_id: String,
    val country: String,
    val device_token: String,
    val device_type: String,
    val email: String,
    val first_name: String,
    val id: String,
    val last_name: String,
    val phone: String,
    val photo: String,
    val status: String,
    val phone_verified: String
)