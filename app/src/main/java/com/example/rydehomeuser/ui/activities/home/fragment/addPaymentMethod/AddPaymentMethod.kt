package com.example.rydehomeuser.ui.activities.home.fragment.addPaymentMethod


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.addCardActivity.AddCardActivity
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.fragment.addCard.Addcard
import com.example.rydehomeuser.ui.activities.home.fragment.paymentPoints.PaymentPoints
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_add_payment_method.view.*


class AddPaymentMethod : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_payment_method, container, false)

        GlobalHelper.setToolbar(getString(R.string.add_payment_method),homeBackIconVis = true)
        clickListener(view)
       // addCard()


        return view
    }

    fun clickListener(view : View)
    {

        Home.homeBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }


        view.lin_cardPayment.setOnClickListener {
            startActivity(Intent(activity as AppCompatActivity, AddCardActivity::class.java))
          //  (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,Addcard()).addToBackStack(null).commit()
           // (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,PaymentPoints()).addToBackStack(null).commit()
        }


    }



/*    fun addCard()
    {
       Luhn.startLuhn(activity,object : LuhnCallback
       {
           override fun cardDetailsRetrieved(p0: Context?, p1: LuhnCard?, p2: LuhnCardVerifier?) {
           }

           override fun onFinished(p0: Boolean) {
           }

           override fun otpRetrieved(p0: Context?, p1: LuhnCardVerifier?, p2: String?) {
           }

       },R.style.LuhnStyle)



    }*/





}
