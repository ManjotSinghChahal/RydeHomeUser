package com.example.rydehomeuser.data.model.applyPromoCode

data class ApplyPromoCode(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)