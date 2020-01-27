package com.example.rydehomeuser.ui.activities.home.fragment.payment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.applyPromoCode.ApplyPromoCode
import com.example.rydehomeuser.data.model.updateBusinessId.UpdateBusinessId
import com.example.rydehomeuser.ui.activities.getCards.GetCards
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.addCard.Addcard
import com.example.rydehomeuser.ui.activities.home.fragment.addPaymentMethod.AddPaymentMethod
import com.example.rydehomeuser.ui.activities.home.fragment.addPromoCode.AddPromoCode
import com.example.rydehomeuser.ui.activities.home.fragment.editAccount.EditAccount
import com.example.rydehomeuser.ui.activities.home.fragment.tip.Tip
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_payment.view.*
import kotlinx.android.synthetic.main.fragment_your_trips.view.*
import java.lang.Exception


class Payment : androidx.fragment.app.Fragment(), HomeContract.PaymentView {



    var presenter: HomeContract.HomePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        presenter = HomePresenterImp(this)
        GlobalHelper.setToolbar(getString(R.string.payment_s),homeCrossIconVis= true)


        clickListener(view)


        return view
    }



    fun clickListener(view : View)
    {
        view.rel_addBussinessProfile.setOnClickListener {
            view.rel_addBussinessProfile.visibility = View.GONE
            view.rel_enterBussinessID.visibility = View.VISIBLE
            Home.homeCrossIcon.visibility = View.GONE
            Home.homeBackIcon.visibility = View.VISIBLE
        }


        Home.homeCrossIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        Home.homeBackIcon.setOnClickListener {
            view.rel_addBussinessProfile.visibility = View.VISIBLE
            view.rel_enterBussinessID.visibility = View.GONE
            Home.homeCrossIcon.visibility = View.VISIBLE
            Home.homeBackIcon.visibility = View.GONE
        }


        view.txtview_addPromoCode.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,AddPromoCode()).addToBackStack(null).commit()
        }

        view.txtview_addPaymentMethod.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,AddPaymentMethod()).addToBackStack(null).commit()
        }


        view.card_Payment.setOnClickListener {
         //   (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,Addcard()).addToBackStack(null).commit()
            startActivity(Intent(activity,GetCards::class.java))
        }

        view.profile_accSettings.setOnClickListener {
            GlobalHelper.replaceFragment(activity as AppCompatActivity, EditAccount(),R.id.container_map)
        }

        view.update_payment.setOnClickListener {

            if (view.edt_buinessId.text.toString().trim().equals("")) {
                GlobalHelper.snackBar(view.rootPayment,activity!!.getString(R.string.enter_business_id))
            }
            else{
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter!!.updateBusinessId(view.edt_buinessId.text.toString().trim())
            }

        }
    }

    override fun onUpdateBusinessIdSuccess(updateBusinessId: UpdateBusinessId) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootPayment,updateBusinessId.message)
        }catch (ex : Exception) {}    }


    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootPayment, it)
            }
        }catch (ex : Exception) {}    }

  }



