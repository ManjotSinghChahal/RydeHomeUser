package com.example.rydehomeuser.data.model.getTrips

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RouteX(
    val end_lat: String,
    val end_long: String,
    val start_lat: String,
    val start_long: String
) : Parcelable