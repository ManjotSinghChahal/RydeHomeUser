package com.example.rydehomeuser.ui.activities.searchPlacesActivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.google.android.gms.location.places.AutocompletePrediction
import kotlinx.android.synthetic.main.searchesplace_adpt.view.*


class SearchPlacesAdpt(val context: Context, val filterData: ArrayList<AutocompletePrediction>, val callback : PlaceSel
) : RecyclerView.Adapter<SearchPlacesAdpt.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.searchesplace_adpt, parent, false))
    }

    override fun getItemCount(): Int {
        return if (filterData == null) 0 else filterData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViews(filterData.get(position).getFullText(null).toString())

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindViews(place: String) {

            itemView.txtview_place.text = place

            itemView.txtview_place.setOnClickListener {
                callback.OnPlaceSel(filterData.get(adapterPosition).getFullText(null).toString())
                (context as AppCompatActivity).onBackPressed()
            }

        }

    }

    interface PlaceSel
    {
        fun OnPlaceSel(place : String)
    }

}