package com.example.rydehomeuser.data.model.getSavedAddress

data class Body(
    val home: Home,
    val others: List<Other>,
    val work: Work
)