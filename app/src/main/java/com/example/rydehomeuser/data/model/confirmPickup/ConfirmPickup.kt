package com.example.rydehomeuser.data.model.confirmPickup

data class ConfirmPickup(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)