package com.example.rydehomeuser.data.model.getCard

data class GetCard(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)