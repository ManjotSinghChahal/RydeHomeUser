package com.example.rydehomeuser.ui.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import kotlinx.android.synthetic.main.booking_cancel_dialog.*


class BookingCancel(val userRideStatus: UserRideStatus?) : DialogFragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.booking_cancel_dialog,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yes_bookingCancel.setOnClickListener {
            dismiss()
            val reasoncancellation = ReasonForCancellation(userRideStatus)
            fragmentManager?.let { it1 -> reasoncancellation.show(it1,"") }
        }

        no_bookingCancel.setOnClickListener { dismiss() }

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
}