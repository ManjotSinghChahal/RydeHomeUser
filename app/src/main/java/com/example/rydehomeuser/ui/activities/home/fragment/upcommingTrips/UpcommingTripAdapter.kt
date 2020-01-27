package com.example.rydehomeuser.ui.activities.home.fragment.upcommingTrips

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Upcoming
import com.example.rydehomeuser.ui.activities.home.fragment.tripDetails.TripDetailsActivity
import com.example.rydehomeuser.utils.Constants.TRIP_DETAILS
import com.example.rydehomeuser.utils.Constants.UPCOMING_TRIPS
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.conCat
import kotlinx.android.synthetic.main.past_trip_adpt.view.*


class UpcommingTripAdapter(
    val context: Context,
    val upcomingList: List<Upcoming>?
) : RecyclerView.Adapter<UpcommingTripAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.past_trip_adpt,container,false))
    }

    override fun getItemCount(): Int {
        return upcomingList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindItems("")
    }


    inner class ViewHolder(itemView : View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView)
    {


        fun bindItems(valCheck : String) {


            // val amountTrips: TextView = itemView.findViewById(R.id.amount_trips) as TextView
            val linLayTrips: LinearLayout = itemView.findViewById(R.id.linLay_trips) as LinearLayout
            val webviewTrips: WebView = itemView.findViewById(R.id.webview_trips) as WebView




            itemView.carName_pastTrips.text = upcomingList!!.get(adapterPosition).vehicle_name
            itemView.date_pastTrips.text = GlobalHelper.getDateTimeFromTimestamp(upcomingList!!.get(adapterPosition).created)
            GlobalHelper.uptoTwoDecimal(upcomingList!!.get(adapterPosition).amount).conCat(itemView, itemView.amount_trips)

            webviewTrips.settings.javaScriptEnabled = true
            webviewTrips.loadUrl("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                    "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                    "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                    "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")






            linLayTrips.setOnClickListener {

                val intent = Intent(context, TripDetailsActivity::class.java)
                intent.putExtra(TRIP_DETAILS,upcomingList.get(adapterPosition))
                intent.putExtra(UPCOMING_TRIPS, UPCOMING_TRIPS)
                context.startActivity(intent)

            }


        }

    }
}