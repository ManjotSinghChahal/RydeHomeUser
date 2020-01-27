package com.example.rydehomeuser.data.model.paymentDone

data class PaymentDone(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
)