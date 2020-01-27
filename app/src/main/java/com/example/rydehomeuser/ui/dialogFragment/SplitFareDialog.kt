package com.example.rydehomeuser.ui.dialogFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.saveData.notificationData.NotificationData
import kotlinx.android.synthetic.main.cancellation_fee.*
import kotlinx.android.synthetic.main.split_fare.*
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class SplitFareDialog(val callback : SplitFareInterface,
                      val notifiData : NotificationData) : DialogFragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.split_fare,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtview_fareSplit.text = "${getString(R.string.fare_split_with)} ${notifiData.fareSplit_name}"
        if (!notifiData.fareSplit_photo.isEmpty())
            Glide.with(this).load(notifiData.fareSplit_photo).into(userImg_fareSplit)

        accept_splitFare.setOnClickListener {
            callback.OnSplitFareSuccess("1",notifiData.ride_id)
            dismiss()
        }

        decline_splitFare.setOnClickListener {
            callback.OnSplitFareSuccess("2",notifiData.ride_id)
            dismiss()
        }

        try {
            Executors.newSingleThreadScheduledExecutor().schedule({

            /* val frag =   activity!!.supportFragmentManager.findFragmentByTag("SpiltFareDialog")
                if (frag!=null)*/
                  callback.OnSplitFareSuccess("2",notifiData.ride_id)
                  dismiss()

            }, 20, TimeUnit.SECONDS)
        }catch (ex : Exception){

        }

    }


    override fun onResume() {
        val params = dialog!!.window!!.attributes
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
        // Call super onResume after sizing
        super.onResume()
    }

    interface SplitFareInterface
    {
        fun OnSplitFareSuccess(status: String,ride_id : String)
    }


}