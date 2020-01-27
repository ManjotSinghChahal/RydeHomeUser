package com.example.rydehomeuser.ui.activities.home.fragment.freeRTrips


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat

import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.GlobalHelper.PROMO_CODE
import com.example.rydehomeuser.utils.conCatReturn
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_free_trips.view.*


class FreeTrips : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_free_trips, container, false)


        GlobalHelper.setToolbar(getString(R.string.free_trips),homeCrossIconVis = true)
        setValues(view)
        clickListener(view)



        return view
    }


    fun setValues(view : View)
    {
        val amt = getString(R.string.share_free_rides)+" "+"30.00".conCatReturn() +" each!"
        view.txtview_share_free_rides.text = amt

        view.invitationCode_freeTrips.text = PROMO_CODE
    }

    fun clickListener(view : View)
    {
        Home.homeCrossIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        view.share_freeTrips.setOnClickListener {

            val data = view.invitationCode_freeTrips.text.toString().trim()
            val link = "http://192.68.35525.14/ryde_home/admin/$data"

            val shareIntent = ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle("Share ")
                .setText(link)
                .intent

            if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
                activity!!.startActivity(shareIntent)
            }
        }
    }


}
