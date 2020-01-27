package com.example.rydehomeuser.data.model.userRideStatus

data class UserRideStatus(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)