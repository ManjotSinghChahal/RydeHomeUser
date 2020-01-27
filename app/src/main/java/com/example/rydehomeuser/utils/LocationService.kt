package com.example.rydehomeuser.utils

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*


class LocationService : Service(), LocationListener {


    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }


    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 2000
    private val FASTEST_INTERVAL: Long = 30000
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest


    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return Service.START_STICKY
    }

    override fun onCreate() {

        mLocationRequest = LocationRequest()

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //   showAlert()
        }

        startLocationUpdates()


    }


    override fun onDestroy() {
        super.onDestroy()
        stoplocationUpdates()
    }


    override fun onLocationChanged(location: Location) {


        Log.e("runLocationMethod","runing")
        Log.e("printLatLng","${location.latitude} ${location.longitude}")

        if (GlobalHelper.CURRENT_LAT == 0.0 && GlobalHelper.CURRENT_LNG == 0.0) {
            GlobalHelper.CURRENT_LAT = location.latitude
            GlobalHelper.CURRENT_LNG = location.longitude
            sendMessageToUI(location.latitude.toString(),location.longitude.toString())

        } else {
            GlobalHelper.CURRENT_LAT = location.latitude
            GlobalHelper.CURRENT_LNG = location.longitude
            sendMessageToUI(location.latitude.toString(),location.longitude.toString())
        }

    }


    //---------------------------------------------


    private fun showAlert() {
        /* val dialog = AlertDialog.Builder(this)
         dialog.setTitle("Enable Location")
             .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
             .setPositiveButton("Location Settings", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                 val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                 startActivity(myIntent)
             })
             .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
         dialog.show()*/
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.setInterval(1000)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {


            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }


    private fun stoplocationUpdates() {
        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
    }


    private fun sendMessageToUI(lat: String, lng: String) {

        val intent = Intent("ACTION_LOCATION_BROADCAST")
        intent.putExtra("lat", lat)
        intent.putExtra("lng", lng)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


}