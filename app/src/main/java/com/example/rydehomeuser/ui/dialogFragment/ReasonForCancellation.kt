package com.example.rydehomeuser.ui.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.data.saveData.cancelReason.Body
import com.example.rydehomeuser.data.saveData.cancelReason.CancelReason
import kotlinx.android.synthetic.main.reason_cancellation_dialog.*

class ReasonForCancellation(val userRideStatus: UserRideStatus?) : DialogFragment(), CancellatonAdapter.CancelRideAdapter
{


    var reason : String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.reason_cancellation_dialog,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cancelList = arrayListOf<Body>()
        cancelList.add(Body("Lost Items",false))
        cancelList.add(Body("Fare Issues",false))
        cancelList.add(Body("Route Feedback",false))
        cancelList.add(Body("Driver Feedback",false))
        cancelList.add(Body("Vehicle Feedback",false))
        cancelList.add(Body("Receipts and Payment",false))
        cancelList.add(Body("I was involved in an accident",false))
        cancelList.add(Body("Card Trips",false))

        val cancelListNew = CancelReason(cancelList)




        val recyclerviewCancellation = view.findViewById(R.id.recyclerview_reasonCancellation) as RecyclerView
        recyclerviewCancellation.layoutManager = LinearLayoutManager(activity)
        recyclerviewCancellation.adapter = CancellatonAdapter((activity as AppCompatActivity),cancelListNew,done_reasonCancellation,this)


        cancel_reasonCancellation.setOnClickListener { dismiss() }

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

    override fun OnCancelRideAdpt(reason: String) {

        if (reason.isEmpty())
            Toast.makeText(context,"Please select atleast one reason.",Toast.LENGTH_SHORT).show()
        else{
            dismiss()
            val feecancellation = CancellationFee(reason,userRideStatus)
            fragmentManager?.let { feecancellation.show(it,"") }
        }

    }



}