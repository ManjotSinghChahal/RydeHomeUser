package com.example.rydehomeuser.ui.activities.home.fragment.paymentPoints


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getCard.GetCard
import com.example.rydehomeuser.data.model.paymentDone.PaymentDone
import com.example.rydehomeuser.data.model.rideDetails.RideDetails
import com.example.rydehomeuser.data.model.userRideStatus.UserRideStatus
import com.example.rydehomeuser.ui.activities.getCards.GetCards
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.mapActivity.MapActivity
import com.example.rydehomeuser.ui.dialogFragment.Rating
import com.example.rydehomeuser.utils.CommonPagerAdapter
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.CARD_ID
import com.example.rydehomeuser.utils.Constants.CVV
import com.example.rydehomeuser.utils.Constants.MAP_ACTIVITY
import com.example.rydehomeuser.utils.Constants.PAYMENT_POINTS
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.GlobalHelper.REWARDS_POINTS
import com.example.rydehomeuser.utils.conCat
import kotlinx.android.synthetic.main.fragment_payment_points.view.*


class PaymentPoints(val userRideStatus: UserRideStatus) : DialogFragment(), HomeContract.PaymentPonitsView {


    lateinit var viewPager: ViewPager
    var presenter: HomeContract.HomePresenter? = null
    var rewardCount: Int = 0
    var rewardPerUnit: Double = 0.0
    var totalAmount: Double = 0.0
    val CARD_TYPE: Int = 4
    var card_id = ""



    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }

  /*  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
       // return super.onCreateDialog(savedInstanceState)

      //  val dialog = builder.create()
        dialog!!.getWindow()!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return dialog!!
    }*/


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_payment_points, container, false)

        getDialog()!!.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initialize(view)
        clickListener(view)


    }

    fun initialize(view: View) {
        presenter = HomePresenterImp(this)
        presenter!!.rideDetails(userRideStatus.body.get(0).ride_id)
        presenter!!.getCard("", PAYMENT_POINTS)



        viewPager = view.findViewById(R.id.viewPager_payment) as ViewPager
        val adapter = CommonPagerAdapter()

        // insert page ids
        adapter.insertViewId(R.id.linLay_help_payment)
        adapter.insertViewId(R.id.linLay_receipt_payment)

        view.view_receipt_payment.visibility = View.VISIBLE
        view.view_help_payment.visibility = View.GONE

        GlobalHelper.uptoTwoDecimal(userRideStatus.body.get(0).amount).conCat(view, view.totalAmt_payment)
        view.edt_enterPoints.isEnabled = false


        // attach adapter to viewpager
        viewPager.setAdapter(adapter)
        viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(pos: Int) {

            }

            override fun onPageScrolled(pos1: Int, pos3: Float, pos2: Int) {

            }

            override fun onPageSelected(pos: Int) {
                if (pos == 0) {
                    view.view_receipt_payment.visibility = View.GONE
                    view.view_help_payment.visibility = View.VISIBLE

                } else {
                    view.view_receipt_payment.visibility = View.VISIBLE
                    view.view_help_payment.visibility = View.GONE
                }
            }

        })
        viewPager.currentItem = 1
    }


    fun clickListener(view: View) {
        view.cross_icon_paymentPoints.setOnClickListener {
            if (view.next_pay_points.text.toString().trim().equals("PAY")) {
                view.rel_totalAmount.visibility = View.GONE
                view.next_pay_points.text = getString(R.string.next)
            } else
                activity?.onBackPressed()
        }

        view.rel_back_paymentsPonts.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }


        view.rel_receipt_payment.setOnClickListener {
            viewPager.currentItem = 1
        }

        view.rel_help_payment.setOnClickListener {
            viewPager.currentItem = 0
        }

        view.rel_payByCard_paymentPoints.setOnClickListener {

           // val frag : Fragment = (context as AppCompatActivity).supportFragmentManager.findFragmentById()

            try {
                if (!view.edt_enterPoints.text.trim().isEmpty())
                    REWARDS_POINTS = view.edt_enterPoints.text.toString().toInt()
            }catch (ex : Exception){}

            val intent = Intent(activity, GetCards::class.java)
            intent.putExtra(PAYMENT_POINTS, PAYMENT_POINTS)
            startActivityForResult(intent, CARD_TYPE)
        }

        view.checkbox_paymentPoints.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {

                view.edt_enterPoints.isEnabled = true
            } else {
                view.edt_enterPoints.setText("")
                view.edt_enterPoints.isEnabled = false
                GlobalHelper.uptoTwoDecimal(userRideStatus.body.get(0).amount).conCat(view, view.totalAmt_payment)
            }
        }

       view.edt_enterPoints.addTextChangedListener(object : TextWatcher{
           override fun afterTextChanged(s: Editable?) {
           }

           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
           }

           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


               try {
                   if ( view.edt_enterPoints.text.toString().trim().toInt() > rewardCount)
                   {
                       Toast.makeText(context,"Entered points should not be more than available points.",Toast.LENGTH_SHORT).show()
                   }
                  else
                   {
                       val rewardAmt : Double = view.edt_enterPoints.text.toString().trim().toDouble() * rewardPerUnit
                       if (rewardAmt>totalAmount)
                       {
                           Toast.makeText(context,"Reward amount should not be more than total amount.",Toast.LENGTH_SHORT).show()
                       }
                       else
                       {
                           val totalAfterDis: Double = totalAmount - rewardAmt
                           GlobalHelper.uptoTwoDecimal(totalAfterDis.toString()).conCat(view, view.totalAmt_payment)
                       }

                   }
               }catch (ex : Exception){}

           }

       })

        view.next_pay_points.setOnClickListener {


            if (view.checkbox_paymentPoints.isChecked) {

                try {

                    if (view.edt_enterPoints.text.toString().trim().isEmpty() || view.edt_enterPoints.text.toString().trim().toInt() < 1) {
                        GlobalHelper.snackBar(view!!.rootPaymentPoints, "Entered points should not be empty.")
                    } else if (view.edt_enterPoints.text.toString().trim().toInt() > rewardCount) {
                        GlobalHelper.snackBar(
                            view!!.rootPaymentPoints,
                            "Entered points should not be more than available points."
                        )

                    } else if (view.edt_enterPoints.text.toString().trim().toInt() <= rewardCount) {

                        val rewardAmt : Double = view.edt_enterPoints.text.toString().trim().toDouble() * rewardPerUnit
                      //  val totalAfterDis: Double = totalAmount - rewardAmt

                        if (rewardAmt > userRideStatus.body.get(0).amount.toDouble()) {
                            GlobalHelper.snackBar(view!!.rootPaymentPoints, "Total amount to be paid is less.")
                        } else {

                            if (MapActivity.cvv_num.isEmpty())
                            {

                                try {
                                    if (!view.edt_enterPoints.text.trim().isEmpty())
                                     REWARDS_POINTS = view.edt_enterPoints.text.toString().toInt()
                                }catch (ex : Exception){}

                              //  Toast.makeText(context,"Please Enter CVV",Toast.LENGTH_SHORT).show()
                                val intent = Intent(activity, GetCards::class.java)
                                intent.putExtra(PAYMENT_POINTS, PAYMENT_POINTS)
                                startActivityForResult(intent, CARD_TYPE)
                            }
                            else
                            {

                                GlobalHelper.showProgress(activity as AppCompatActivity)
                                presenter!!.paymentDone(userRideStatus!!.body.get(0).ride_id, card_id, view.edt_enterPoints.text.toString().trim(),MapActivity.cvv_num)
                                REWARDS_POINTS = 0
                            }

                        }

                    }
                } catch (ex: Exception) {

                }

            } else {

                if (card_id.isEmpty())
                    GlobalHelper.snackBar(view!!.rootPaymentPoints, "Please Select Card")
                else if (MapActivity.cvv_num.isEmpty())
                {
                 //   Toast.makeText(context,"Please Enter CVV",Toast.LENGTH_SHORT).show()
                    try {
                        if (!view.edt_enterPoints.text.trim().isEmpty())
                          REWARDS_POINTS = view.edt_enterPoints.text.toString().toInt()
                    }catch (ex : Exception){}

                    val intent = Intent(activity, GetCards::class.java)
                    intent.putExtra(PAYMENT_POINTS, PAYMENT_POINTS)
                    startActivityForResult(intent, CARD_TYPE)

                }
                else
                {
                    GlobalHelper.showProgress(activity as AppCompatActivity)
                    presenter!!.paymentDone(userRideStatus!!.body.get(0).ride_id, card_id, "0",MapActivity.cvv_num)
                    REWARDS_POINTS = 0

                }

            }


        }


    }


    override fun onPaymentPonitsSuccess(rideDetails: RideDetails) {
        try {
            GlobalHelper.hideProgress()
            rideDetails.message.let {
             //   GlobalHelper.snackBar(view!!.rootPaymentPoints, it)
                if (rideDetails.code.equals(200)) {

                    setValues(view!!, rideDetails)
                }

            }
        } catch (ex: Exception) {
        }
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let {
                GlobalHelper.snackBar(view!!.rootPaymentPoints, it)
            }
        } catch (ex: Exception) {
        }
    }

    fun setValues(view: View, rideDetails: RideDetails) {


        try {
            if (!rideDetails.body.rewards.isEmpty())
                rewardCount = rideDetails.body.rewards.toInt()
            if (!rideDetails.body.reward_value.isEmpty())
                rewardPerUnit = rideDetails.body.reward_value.toDouble()
            if (!rideDetails.body.amount.isEmpty())
                totalAmount = rideDetails.body.amount.toDouble()

        } catch (ex: Exception) {
        }

        view!!.carType_paymentPoints.text = rideDetails.body.vehicle_name
        view!!.sourceLoc_paymentPoints.text = rideDetails.body.from_address
        view!!.destLoc_paymentPoints.text = rideDetails.body.to_address
        view!!.tripwith_paymentPoints.text = "${getString(R.string.your_trip_with)} ${rideDetails.body.driver_name}"


        // Glide.with(activity as AppCompatActivity).load(rideDetails.body.toString()).into(view!!.userImg_payment)

        view!!.datTime_paymentPoints.text = GlobalHelper.getDateTimeFromTimestamp(rideDetails.body.created)







        if (rideDetails.body.is_splitted.equals("0")) {


            GlobalHelper.uptoTwoDecimal(rideDetails.body.amount).conCat(view, view.amount_trip_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.amount).conCat(view, view.total_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.amount).conCat(view, view.paidByCard_payment)

            GlobalHelper.uptoTwoDecimal(rideDetails.body.amount).conCat(view, view.tripFare_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.amount).conCat(view, view.subTotal_payment)
        } else {
            GlobalHelper.uptoTwoDecimal(rideDetails.body.splitted_amount).conCat(view, view.amount_trip_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.splitted_amount).conCat(view, view.total_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.splitted_amount).conCat(view, view.paidByCard_payment)

            GlobalHelper.uptoTwoDecimal(rideDetails.body.splitted_amount).conCat(view, view.tripFare_payment)
            GlobalHelper.uptoTwoDecimal(rideDetails.body.splitted_amount).conCat(view, view.subTotal_payment)
        }

        view!!.avaiPoints_paymentPoints.text = "Avl: ${GlobalHelper.uptoTwoDecimal(rideDetails.body.rewards)}pt"


        view.webview_map_payment.settings.javaScriptEnabled = true
        view.webview_map_payment.loadUrl(
            "https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap\n" +
                    "        &markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318\n" +
                    "        &markers=color:red%7Clabel:C%7C40.718217,-73.998284\n" +
                    "        &key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE"
        )


        if (REWARDS_POINTS!=0)
        {

            try {
                view.edt_enterPoints.setText(REWARDS_POINTS.toString())
                val rewardAmt : Double = REWARDS_POINTS * rewardPerUnit

                val totalAfterDis: Double = totalAmount - rewardAmt
                GlobalHelper.uptoTwoDecimal(totalAfterDis.toString()).conCat(view, view.totalAmt_payment)

                view.edt_enterPoints.isEnabled = true
                view.checkbox_paymentPoints.isChecked = true

            }catch (ex : Exception){}

        }

    }

    override fun onPaymentDoneSuccess(paymentDone: PaymentDone) {
        try {
            GlobalHelper.hideProgress()
            paymentDone.message.let {
                GlobalHelper.snackBar(view!!.rootPaymentPoints, it)
                if (paymentDone.code.equals(200)) {
                    // activity?.let { it.onBackPressed() }
                    dismissAllowingStateLoss()
                    MapActivity.cvv_num = ""

                    val ft = activity?.supportFragmentManager!!.beginTransaction()
                    val rating = Rating(userRideStatus)
                    rating.show(ft, "")
                    rating.isCancelable = false

                }

            }
        } catch (ex: Exception) {
        }
    }

    override fun onGetCardSuccess(getCard: GetCard) {

        try {
            if (getCard.code.equals(200)) {
                for (i in 0..getCard.body.size - 1) {
                    if (getCard.body.get(i).selected_card.equals("1"))
                        card_id = getCard.body.get(i).id

                }
            }
        } catch (ex: Exception) {
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null)
        {

            if (resultCode == Activity.RESULT_OK) {

                try {

                    //----for card select----------------------
                    if (requestCode == CARD_TYPE)
                    {
                        card_id = data!!.getStringExtra(CARD_ID)
                        MapActivity.cvv_num = data!!.getStringExtra(CVV)

                    }


                }catch (ex : Exception){}
            }
        }


    }
}
