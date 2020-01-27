package com.example.rydehomeuser.ui.activities.home.fragment.tripDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Past
import com.example.rydehomeuser.data.model.getTrips.Upcoming
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.utils.CommonPagerAdapter
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.conCat
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.activity_trip_details.amount_trip_details
import kotlinx.android.synthetic.main.activity_trip_details.carNamr_tripDetails
import kotlinx.android.synthetic.main.activity_trip_details.dateTime_tripDetails
import kotlinx.android.synthetic.main.activity_trip_details.drivername_tripDetails
import kotlinx.android.synthetic.main.activity_trip_details.endAddr_tripDetails
import kotlinx.android.synthetic.main.activity_trip_details.paidByCard_trip_details
import kotlinx.android.synthetic.main.activity_trip_details.rel_help
import kotlinx.android.synthetic.main.activity_trip_details.rel_receipt
import kotlinx.android.synthetic.main.activity_trip_details.startAddr_tripDetails
import kotlinx.android.synthetic.main.activity_trip_details.subTotal_trip_details
import kotlinx.android.synthetic.main.activity_trip_details.total_trip_details
import kotlinx.android.synthetic.main.activity_trip_details.tripFare_trip_details
import kotlinx.android.synthetic.main.activity_trip_details.view_help
import kotlinx.android.synthetic.main.activity_trip_details.view_receipt
import kotlinx.android.synthetic.main.activity_trip_details.webview_map_detail

class TripDetailsActivity : AppCompatActivity() {

    lateinit var  viewPager : androidx.viewpager.widget.ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)


        setValues()
        clickListener()

        val bundle = intent.extras
        if (bundle!=null && bundle.containsKey(Constants.TRIP_DETAILS) && bundle.containsKey(Constants.UPCOMING_TRIPS))
        {
            val list : Upcoming =  bundle.getParcelable(Constants.TRIP_DETAILS)


            carNamr_tripDetails.text = list.vehicle_name
            dateTime_tripDetails.text = GlobalHelper.getDateTimeFromTimestamp(list.created)
            drivername_tripDetails.text = "${getString(R.string.your_trip_with)} ${list.driver_name}"
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,amount_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,paidByCard_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,total_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,subTotal_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,tripFare_trip_details)

            Glide.with(this).load(list).into(userImg_tripDetails)


            if (list.route.size>0)
            {
                val start_address = GlobalHelper.getCompleteAddressFromLatLng(
                    list.route.get(0).start_lat.toDouble(),
                    list.route.get(0).start_long.toDouble()
                    ,this)
                val end_address = GlobalHelper.getCompleteAddressFromLatLng(
                    list.route.get(list.route.size-1).end_lat.toDouble(),
                    list.route.get(list.route.size-1).end_long.toDouble(),
                    this)

                startAddr_tripDetails.text = start_address
                endAddr_tripDetails.text = end_address


            }

            rel_paidByCard_tripDetails.visibility = View.GONE
            rel_paidByCardView_tripDetails.visibility = View.GONE
        }
        else if (bundle!=null && bundle.containsKey(Constants.TRIP_DETAILS) && bundle.containsKey(Constants.PAST_TRIPS))
        {
            val list : Past =  bundle.getParcelable(Constants.TRIP_DETAILS)


            carNamr_tripDetails.text = list.vehicle_name
            dateTime_tripDetails.text = GlobalHelper.getDateTimeFromTimestamp(list.created)
            drivername_tripDetails.text = "${getString(R.string.your_trip_with)} ${list.driver_name}"
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,amount_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,paidByCard_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,total_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,subTotal_trip_details)
            GlobalHelper.uptoTwoDecimal(list.amount).conCat(rootTripDetails,tripFare_trip_details)

            if (list.route.size>0)
            {
                val start_address = GlobalHelper.getCompleteAddressFromLatLng(
                    list.route.get(0).start_lat.toDouble(),
                    list.route.get(0).start_long.toDouble()
                    ,this)
                val end_address = GlobalHelper.getCompleteAddressFromLatLng(
                    list.route.get(list.route.size-1).end_lat.toDouble(),
                    list.route.get(list.route.size-1).end_long.toDouble(),
                    this)


                startAddr_tripDetails.text = start_address
                endAddr_tripDetails.text = end_address


            }


        }
    }

    fun setValues()
    {

        viewPager = findViewById(R.id.viewPager_tripDetails) as androidx.viewpager.widget.ViewPager
        val adapter = CommonPagerAdapter()

        // insert page ids
        adapter.insertViewId(R.id.linLay_help)
        adapter.insertViewId(R.id.linLay_receipt)

        view_receipt.visibility = View.GONE
        view_help.visibility = View.VISIBLE


        // attach adapter to viewpager
        viewPager.setAdapter(adapter)
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(pos: Int) {

            }

            override fun onPageScrolled(pos1: Int, pos3: Float, pos2: Int) {

            }

            override fun onPageSelected(pos: Int) {
                if (pos==0)
                {
                    view_receipt.visibility = View.GONE
                    view_help.visibility = View.VISIBLE

                }
                else
                {
                    view_receipt.visibility = View.VISIBLE
                    view_help.visibility = View.GONE
                }
            }

        })







        "60.41".conCat(rootTripDetails, amount_trip_details)
        "60.00".conCat(rootTripDetails, tripFare_trip_details)
        "60.00".conCat(rootTripDetails, subTotal_trip_details)
        "60.00".conCat(rootTripDetails, total_trip_details)
        "60.00".conCat(rootTripDetails, paidByCard_trip_details)



        webview_map_detail.settings.javaScriptEnabled = true
        webview_map_detail.loadUrl("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")

    }


    fun clickListener()
    {
        relBack_tripDetailsAct.setOnClickListener {
          onBackPressed()
            /*finishAffinity()
            startActivity(Intent(this,Home::class.java))*/
        }

        rel_receipt.setOnClickListener {
            viewPager.currentItem = 1
        }

        rel_help.setOnClickListener {
            viewPager.currentItem = 0
        }
    }
}
