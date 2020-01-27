package com.example.rydehomeuser.data.model.phoneVerify

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhoneVerify(
    val body: Body,
    val code: Int,
    val message: String,
    val success: Boolean
) : Parcelable