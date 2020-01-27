package com.example.rydehomeuser.ui.activities.home.fragment.help

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Past
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.conCat
import kotlinx.android.synthetic.main.help_adapter.view.*


class HelpAdapter(
    val context: Context,
    val past: List<Past>,
    val callback : ReportIssue) : RecyclerView.Adapter<HelpAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.help_adapter,container,false))
    }

    override fun getItemCount(): Int {
        return past.size
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.bindviews(past)
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {

        fun bindviews(past: List<Past>)
        {
            itemView.carName_help.text = past.get(adapterPosition).vehicle_name
            GlobalHelper.uptoTwoDecimal(past.get(adapterPosition).amount).conCat(itemView,itemView.amount_help)

            itemView.reportIssue_help.setOnClickListener {
                callback.onReportIssueSuccess(past.get(adapterPosition).previous_ride_id)

                val webviewHelp: WebView = itemView.findViewById(R.id.webview_help) as WebView

                webviewHelp.settings.javaScriptEnabled = true
                webviewHelp.loadUrl(
                    "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                            "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                            "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                            "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE"
                )
            }


        }
    }

    interface ReportIssue
    {
        fun onReportIssueSuccess(ride_id : String)
    }
}