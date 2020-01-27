package com.example.rydehomeuser.data.model.contactList

data class ContactList(
    val body: List<Body>,
    val code: Int,
    val message: String,
    val success: Boolean
)