package com.example.rydehomeuser.data.model.carRequests

data class CarRequests(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)