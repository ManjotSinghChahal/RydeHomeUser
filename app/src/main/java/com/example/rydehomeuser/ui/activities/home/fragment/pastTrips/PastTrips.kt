package com.example.rydehomeuser.ui.activities.home.fragment.pastTrips


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Past


class PastTrips(val title : String, val pastList : List<Past>) : Fragment() {

   // var pastList  : List<Past>? = null

   /* constructor( value: String,  list : List<Past>) : this() {
        pastList = list
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_past_trips, container, false)


        setUp(view)



        return view
    }

    fun setUp(view : View)
    {

        val recyclerviewPastTrips = view.findViewById(R.id.recyclerview_past_trips) as androidx.recyclerview.widget.RecyclerView
        recyclerviewPastTrips.layoutManager = LinearLayoutManager(activity)
        activity?.let {

            if (pastList!=null)
                recyclerviewPastTrips.adapter = PastTripAdapter(it,pastList)

            else{
                val pastListNew : List<Past> = arrayListOf<Past>()
                recyclerviewPastTrips.adapter = PastTripAdapter(it,pastListNew)
            }

        }
    }






}
