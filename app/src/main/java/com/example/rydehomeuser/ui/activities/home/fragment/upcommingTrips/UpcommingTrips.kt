package com.example.rydehomeuser.ui.activities.home.fragment.upcommingTrips


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getTrips.Upcoming


class UpcommingTrips()  : androidx.fragment.app.Fragment() {

    var upcomingList : List<Upcoming>? = null
    @SuppressLint("ValidFragment")
    constructor( value: String, upcommingList : List<Upcoming>) : this() {
        upcomingList = upcommingList
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_upcomming_trips, container, false)


        setUp(view)

        return view
    }



    fun setUp(view : View)
    {
        val recyclerviewUpcomingTrips = view.findViewById(R.id.recyclerview_upcomming_trips) as androidx.recyclerview.widget.RecyclerView
        recyclerviewUpcomingTrips.layoutManager = activity?.let { androidx.recyclerview.widget.LinearLayoutManager(it) }
        activity?.let {
            if (upcomingList!=null)
            recyclerviewUpcomingTrips.adapter = UpcommingTripAdapter(it, upcomingList)
            else{
                val upComingListNew : List<Upcoming> = arrayListOf<Upcoming>()
                recyclerviewUpcomingTrips.adapter = UpcommingTripAdapter(it,upComingListNew)
            }
        }
    }

}
