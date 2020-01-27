package com.example.rydehomeuser.data.model.phoneVerify

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Body(
    val authorization_key: String,
    val content: String,
    val otp: Int,
    val id: String,
    val phone_verfied: String
) : Parcelable