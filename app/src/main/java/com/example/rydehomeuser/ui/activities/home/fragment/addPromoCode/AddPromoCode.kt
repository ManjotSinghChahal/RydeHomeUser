package com.example.rydehomeuser.ui.activities.home.fragment.addPromoCode


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.applyPromoCode.ApplyPromoCode
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_add_promo_code.view.*
import kotlinx.android.synthetic.main.fragment_payment.view.*
import java.lang.Exception


class AddPromoCode : Fragment(), HomeContract.ApplyPromoCodeView {
    var presenter: HomeContract.HomePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_promo_code, container, false)



        GlobalHelper.setToolbar(getString(R.string.add_promo_code), homeBackIconVis = true)
        presenter = HomePresenterImp(this)

        clickListener(view)





        return view
    }


    fun clickListener(view: View) {

        Home.homeBackIcon.setOnClickListener {
            GlobalHelper.hideSoftKeyBoard(activity as AppCompatActivity, view.rootPromoCode)
            activity?.let { it.onBackPressed() }
        }

        view.add_Code.setOnClickListener {

            if (view.applyCode_Code.text.toString().trim().isEmpty())
                GlobalHelper.snackBar(view!!.rootPromoCode, getString(R.string.enter_code))
            else{
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter!!.applyPromoCode(view.applyCode_Code.text.toString().trim())
            }

        }

    }


    override fun onApplyPromoCodeSuccess(applyPromoCode: ApplyPromoCode) {
        try {
            GlobalHelper.hideProgress()
            applyPromoCode.message.let {
                GlobalHelper.snackBar(view!!.rootPromoCode, it)
                if (applyPromoCode.code.equals(200))
                    view!!.applyCode_Code.setText("")
            }
        } catch (ex: Exception) {
        }
    }


    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootPromoCode, error)
        } catch (ex: Exception) {
        }
      }


    }

