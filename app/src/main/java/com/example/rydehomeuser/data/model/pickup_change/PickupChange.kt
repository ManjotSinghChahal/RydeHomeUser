package com.example.rydehomeuser.data.model.pickup_change

data class PickupChange(
    val body: List<Any>,
    val code: Int,
    val message: String,
    val success: Boolean
)