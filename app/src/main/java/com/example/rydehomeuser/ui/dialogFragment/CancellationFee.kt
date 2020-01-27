package com.example.rydehomeuser.ui.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.cancelRide.CancelRide
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.cancellation_fee.*

class CancellationFee(
    val reason: String,
    val userRideStatus: UserRideStatus?
) : DialogFragment(), HomeContract.CancelRideView
{


    var presenter: HomeContract.HomePresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.cancellation_fee,container,false)

        presenter = HomePresenterImp(this)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ok_cancellationFee.setOnClickListener {
           
            GlobalHelper.showProgress(activity as AppCompatActivity)
            presenter!!.cancelRide(userRideStatus!!.body.get(0).ride_id,"5",reason)

            /*dismiss()
            val rating = Rating()
            rating.show(fragmentManager,"")*/
        }
        cancel_cancellationFee.setOnClickListener { dismiss() }
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

    override fun onCancelRideSuccess(cancelRide: CancelRide) {
        try {

            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootCancellationFee,cancelRide.message)

            if (cancelRide.code.equals(200))
            {
                dismiss()
                activity?.let { it.onBackPressed() }


              /*  val rating = Rating(ride_id)
                rating.show(fragmentManager,"")*/
            }
        }catch (ec : Exception){}
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootCancellationFee,error)
        }catch (ec : Exception){}
    }


}