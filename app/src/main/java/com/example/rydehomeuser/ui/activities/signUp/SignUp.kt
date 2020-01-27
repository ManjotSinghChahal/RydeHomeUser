package com.example.rydehomeuser.ui.activities.signUp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.signUp.fragments.chooseType.ChooseType
import com.example.rydehomeuser.utils.LocationService
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class SignUp : AppCompatActivity()
{
    private val REQUEST_PERMISSION_LOCATION = 10
    companion object
    {
        lateinit var signUpBackIcon : ImageView
        lateinit var signUpCrossIcon : ImageView
        lateinit var title_signUp : TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        setId()




        supportFragmentManager.beginTransaction().replace(R.id.container_signup,ChooseType()).commit()


    }

    fun setId()
    {
        signUpBackIcon = findViewById<ImageView>(R.id.signUp_back_icon)
        signUpCrossIcon = findViewById<ImageView>(R.id.signUp_cross_icon)
        title_signUp = findViewById<TextView>(R.id.title_signUp)


    }

    override fun onResume() {
        super.onResume()

        if (checkPermissionForLocation(this)) {
            startService(Intent(this, LocationService::class.java))
        }
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startService(Intent(this, LocationService::class.java))

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, LocationService::class.java))
    }


}


