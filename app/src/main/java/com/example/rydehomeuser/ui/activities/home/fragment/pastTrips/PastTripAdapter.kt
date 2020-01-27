package com.example.rydehomeuser.ui.activities.home.fragment.pastTrips

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Past
import com.example.rydehomeuser.ui.activities.home.fragment.tripDetails.TripDetailsActivity
import com.example.rydehomeuser.utils.Constants.PAST_TRIPS
import com.example.rydehomeuser.utils.Constants.TRIP_DETAILS
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.conCat
import kotlinx.android.synthetic.main.past_trip_adpt.view.*
import java.sql.Timestamp
import java.util.*

class PastTripAdapter(
    val context: Context,
    val pastList: List<Past>?
) : RecyclerView.Adapter<PastTripAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(context).inflate(R.layout.past_trip_adpt,container,false))
    }

    override fun getItemCount(): Int {
        return pastList!!.size
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

            val stamp = Timestamp(pastList!!.get(adapterPosition).created.toLong()*1000L)
            val date = Date(stamp.getTime())



            itemView.carName_pastTrips.text = pastList!!.get(adapterPosition).vehicle_name
            itemView.date_pastTrips.text = GlobalHelper.getDateTimeFromTimestamp(pastList!!.get(adapterPosition).created)
            GlobalHelper.uptoTwoDecimal(pastList!!.get(adapterPosition).amount).conCat(itemView, itemView.amount_trips)

            webviewTrips.settings.javaScriptEnabled = true



            if (pastList.get(adapterPosition).route.size==2)
            {
                webviewTrips.loadUrl("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                        "        &markers=color:blue%7Clabel:S%7C${pastList.get(adapterPosition).route.get(0).start_lat},${pastList.get(adapterPosition).route.get(0).start_long}&markers=color:green%7Clabel:G%${pastList.get(adapterPosition).route.get(1).start_lat},${pastList.get(adapterPosition).route.get(1).start_long}\n" +
                        /*"        &markers=color:red%7Clabel:C%7C${pastList.get(adapterPosition).route.get(2).start_lat},${pastList.get(adapterPosition).route.get(2).start_long}\n" +*/
                        "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")
            }
            else if (pastList.get(adapterPosition).route.size==3)
            {
                webviewTrips.loadUrl("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                        "        &markers=color:blue%7Clabel:S%7C${pastList.get(adapterPosition).route.get(0).start_lat},${pastList.get(adapterPosition).route.get(0).start_long}&markers=color:green%7Clabel:G%${pastList.get(adapterPosition).route.get(1).start_lat},${pastList.get(adapterPosition).route.get(1).start_long}\n" +
                        "        &markers=color:red%7Clabel:C%7C${pastList.get(adapterPosition).route.get(2).start_lat},${pastList.get(adapterPosition).route.get(2).start_long}\n" +
                        "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")
            }






            linLayTrips.setOnClickListener {

                val intent = Intent(context, TripDetailsActivity::class.java)
                intent.putExtra(TRIP_DETAILS,pastList.get(adapterPosition))
                intent.putExtra(PAST_TRIPS,PAST_TRIPS)
                context.startActivity(intent)

             }


        }

    }
}