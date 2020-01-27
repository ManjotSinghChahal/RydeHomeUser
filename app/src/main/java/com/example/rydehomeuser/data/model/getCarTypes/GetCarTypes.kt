package com.example.rydehomeuser.data.model.getCarTypes

data class GetCarTypes(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)