package com.example.rydehomeuser.data.model.register

data class Register(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)