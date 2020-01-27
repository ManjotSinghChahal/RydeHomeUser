package com.example.rydehomeuser.data.model.getWalletRewards

data class Ride(
    val Driver: Driver,
    val amount: String,
    val created: String,
    val driver_id: String,
    val id: String,
    val payment_status: String,
    val points_earned: String,
    val ride_details: RideDetails,
    val ride_id: String,
    val transaction_id: String,
    val user_id: String
)