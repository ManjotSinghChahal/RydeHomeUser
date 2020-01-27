package com.example.rydehomeuser.data.model.splitRequest

data class SplitRequest(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)