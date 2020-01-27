package com.example.rydehomeuser.ui.activities.home.fragment.otpScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.editAccount.EditAccount
import com.example.rydehomeuser.ui.activities.signUp.fragments.enterName.EnterName
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.Constants.OTP_SCREEN
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.choosecab_adapter.*
import kotlinx.android.synthetic.main.fragment_mobile_number.view.*
import kotlinx.android.synthetic.main.fragment_otp.view.*

class OtpScreen  : Fragment() , HomeContract.VerifyOtpView{


    var presenter: HomeContract.HomePresenter? = null
    var data: LoginData? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_otp, container, false)


        initialize(view)
        clickListener(view)


        return view
    }

    fun initialize(view: View) {
        /* SignUp.signUpBackIcon.visibility = View.VISIBLE
        SignUp.signUpCrossIcon.visibility = View.GONE
        SignUp.title_signUp.visibility = View.VISIBLE
        SignUp.title_signUp.text = getString(R.string.txt_verify_mobile)*/

        GlobalHelper.setToolbar(getString(R.string.txt_verify_mobile), homeBackIconVis = true)

        presenter = HomePresenterImp(this)

        val bundle = arguments
        if (bundle != null && bundle.containsKey(Constants.LOGIN_DATA)) {
            data = bundle.getParcelable(Constants.LOGIN_DATA)
            view.txtview_info_otp.text =
                "${activity?.getString(R.string.inst_verify_mobile)} ${data!!.c_code} ${data!!.phone}"
        }

    }


    fun clickListener(view: View) {


        /* SignUp.signUpBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }*/

        view.txt_otp_submit.setOnClickListener {




            if (view.txt_pin_entry.text.toString().trim().isEmpty()) {
                GlobalHelper.snackBar(view.rootOtp, getString(R.string.txt_enter_otp))
            } else if (view.txt_pin_entry.text.toString().trim().equals(data!!.code)) {
              //  presenter?.otpVerifiy(data!!.code,data!!.phone)

                val bundle = Bundle()
                bundle.putString(OTP_SCREEN,OTP_SCREEN)
                bundle.putString("code",data!!.c_code)
                bundle.putString("phone",data!!.phone)
                val frag = EditAccount()
                frag.arguments = bundle

                activity?.supportFragmentManager!!.popBackStack()
                activity?.supportFragmentManager!!.popBackStack()
                activity?.supportFragmentManager!!.popBackStack()


                GlobalHelper.hideSoftKeyBoard(activity as AppCompatActivity,view.rootOtp)
                GlobalHelper.replaceFragment(activity as AppCompatActivity,frag,R.id.container_map)

            } else {
                GlobalHelper.snackBar(view.rootOtp, getString(R.string.otp_not_match))
            }


        }

        Home.homeBackIcon.setOnClickListener {
            GlobalHelper.hideSoftKeyBoard(activity as AppCompatActivity,view.rootOtp)
            activity?.let { it.onBackPressed() }
        }

    }

    override fun onVerifyOtpSuccess(otpVerified: OtpVerified) {
        GlobalHelper.hideProgress()
        otpVerified.message.let { GlobalHelper.snackBar(view!!.rootOtp, it) }

        activity?.supportFragmentManager!!.popBackStack()
        activity?.supportFragmentManager!!.popBackStack()
        activity?.supportFragmentManager!!.popBackStack()


        GlobalHelper.replaceFragment(activity as AppCompatActivity,EditAccount(),R.id.container_map)

    }

    override fun onFailure(error: String) {
        GlobalHelper.hideProgress()
        error.let { GlobalHelper.snackBar(view!!.rootOtp, it) }
    }
}

