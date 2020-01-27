package com.example.rydehomeuser.data.model.addCard

data class Card(
    val card_expiry_month: String,
    val card_expiry_year: String,
    val card_number: String,
    val card_type: String,
    val cardholdername: String,
    val country: String,
    val created: String,
    val id: String,
    val modified: String,
    val postal_code: String,
    val user_id: String
)