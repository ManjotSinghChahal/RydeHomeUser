package com.example.rydehomeuser.data.model.help

data class HelpModel(
    val body: List<Any>,
    val code: Int,
    val message: String,
    val success: Boolean
)