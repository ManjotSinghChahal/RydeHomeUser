package com.example.rydehomeuser.data.model.userRideStatus

data class Body(
    val ride_id: String,
    val amount: String,
    val booking_date: String,
    val distance: String,
    val created: String,
    val average_rating: String,
    val vehicle_number: String,
    val vehicle_type_name: String,
    val driver: Driver,
    val from_address: String,
    val to_address: String,
    val from_lat: String,
    val from_long: String,
    val status: String,
    val time: String,
    val to_lat: String,
    val to_long: String,
    val user: User,
    val vehicle_type_id: String,
    val estimated_price : String
)