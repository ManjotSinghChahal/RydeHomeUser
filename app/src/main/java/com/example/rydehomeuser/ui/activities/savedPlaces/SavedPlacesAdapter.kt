package com.example.rydehomeuser.ui.activities.savedPlaces

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getSavedAddress.Body
import kotlinx.android.synthetic.main.savedplaces_adapter.view.*


class SavedPlacesAdapter(val context: Context, val otherSavedAddresses: Body, val callback: SavedPlacesInterface) :
    RecyclerView.Adapter<SavedPlacesAdapter.ViewHolder>() {



    override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.savedplaces_adapter, container, false))
    }

    override fun getItemCount(): Int {
        return otherSavedAddresses.others.size
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bindviews(otherSavedAddresses)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bindviews(otherSavedAddresses: Body) {

            itemView.otherAddress_savedPlaces.text = otherSavedAddresses.others.get(adapterPosition).address
            itemView.imgCross_otherAddress_savedPlaces.setOnClickListener {
                callback.OnSavedPlacesDel(otherSavedAddresses.others.get(adapterPosition).id)
            }

        }
    }

    interface SavedPlacesInterface
    {
        fun OnSavedPlacesDel(address_id : String)
    }

}