package com.example.rydehomeuser.ui.dialogFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.ratingModel.RatingModel
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.tip.Tip
import com.example.rydehomeuser.utils.Constants.USER_RATING_STATUS
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.rating_dialog.*

class Rating(val userRideStatus: UserRideStatus?) : DialogFragment(), HomeContract.RatingView {


    var presenter: HomeContract.HomePresenter? = null
    var ride_id = ""
    var driver_name = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.rating_dialog, container, false)

        presenter = HomePresenterImp(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (userRideStatus != null && userRideStatus!!.body.size > 0) {
            driver_name = userRideStatus!!.body.get(0).driver.first_name
            ride_id = userRideStatus!!.body.get(0).ride_id
            driverName_ratingDialog.text = "${userRideStatus!!.body.get(0).driver.first_name} ${userRideStatus!!.body.get(0).driver.last_name}"
            Glide.with(activity as AppCompatActivity).load(userRideStatus!!.body.get(0).driver.image).into(driverImage_ratingDialog)

        }


        ratingBar_review.setOnRatingBarChangeListener({ ratingBar, rating, fromUser ->


            if (rating.toString().equals("1.0"))
                ratingTxt_ratingDialog.text = getString(R.string.poor)
            else if (rating.toString().equals("2.0"))
                ratingTxt_ratingDialog.text = getString(R.string.good)
            else if (rating.toString().equals("3.0"))
                ratingTxt_ratingDialog.text = getString(R.string.avg)
            else if (rating.toString().equals("4.0"))
                ratingTxt_ratingDialog.text = getString(R.string.very_good)
            else if (rating.toString().equals("5.0"))
                ratingTxt_ratingDialog.text = getString(R.string.excellent)


        })

        submit_rating.setOnClickListener {

            if (review_rating.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(rootRatingDialog, "Please Enter note.")
            else {
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter!!.rating(
                    userRideStatus!!.body.get(0).ride_id,
                    ratingBar_review.rating.toString(),
                    review_rating.text.toString().trim(),
                    USER_RATING_STATUS
                )
            }
        }

        rel_cross_rating.setOnClickListener {
            presenter!!.rating(userRideStatus!!.body.get(0).ride_id, "0", "", USER_RATING_STATUS)

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

    override fun onRatingSuccess(ratingModel: RatingModel) {

        try {

            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootRatingDialog, ratingModel.message)
            if (ratingModel.code.equals(200)) {

                GlobalHelper.clearAllNotification(activity as AppCompatActivity)

                dismiss()
                dismiss()
                val ft = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val rating = Tip(ride_id,driver_name)
                rating.show(ft, "")
                /*activity?.let {
                    it.finishAffinity()
                    it.startActivity(Intent(it, Home::class.java))
                    it.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                }*/

            }
        } catch (ec: Exception) {
        }
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(rootRatingDialog, error)
        } catch (ec: Exception) {
        }
    }

}