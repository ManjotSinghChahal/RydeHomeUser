package com.example.rydehomeuser.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rydehomeuser.R
import kotlinx.android.synthetic.main.trip_details.*
import xyz.belvi.luhn.BaseActivity

class TripDemo : AppCompatActivity()

{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_details)

        webview_map_Demodetail.settings.javaScriptEnabled = true
        webview_map_Demodetail.loadUrl("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")

        back_tripDemo.setOnClickListener {
            finish()
        }

    }

}