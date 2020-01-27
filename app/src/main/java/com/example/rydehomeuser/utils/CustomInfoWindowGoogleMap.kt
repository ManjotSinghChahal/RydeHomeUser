package com.example.rydehomeuser.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.rydehomeuser.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowGoogleMap(
    private val context: Context
) : GoogleMap.InfoWindowAdapter {


    override fun getInfoContents(marker: Marker?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.map_infowindow, null)

        return view
    }

    override fun getInfoWindow(marker: Marker?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.map_infowindow, null)




        return view
    }





}
