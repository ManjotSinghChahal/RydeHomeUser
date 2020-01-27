package com.example.rydehomeuser.ui.activities.home.fragment.enterMobile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.model.sendOtp.SendOtp
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.activities.home.fragment.otpScreen.OtpScreen
import com.example.rydehomeuser.utils.Constants
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.Validator
import kotlinx.android.synthetic.main.fragment_mobile_number.*
import kotlinx.android.synthetic.main.fragment_mobile_number.view.*

class EnterMobile  : Fragment(), HomeContract.SendOtpView {



    var presenter: HomeContract.HomePresenter? = null
    var phoneVal = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_number, container, false)


        initialize(view)
        clickListener(view)


        return view
    }


    fun initialize(view: View) {
       /* SignUp.signUpBackIcon.visibility = View.GONE
        SignUp.signUpCrossIcon.visibility = View.VISIBLE
        SignUp.title_signUp.visibility = View.VISIBLE
        SignUp.title_signUp.text = getString(R.string.mobilenum)*/

        GlobalHelper.setToolbar(getString(R.string.mobilenum), homeBackIconVis = true)


        presenter = HomePresenterImp(this)

    }


    fun clickListener(view: View) {
       /* SignUp.signUpCrossIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }*/

        view.next_mobileNum.setOnClickListener {

            phoneVal =
                "${view.countryCode_phoneVerify.selectedCountryCode}${view.phoneNo_phoneVerify.text.toString().trim()}"

            if (Validator.getsInstance().phoneValidator(
                    view.phoneNo_phoneVerify.text.toString().trim(),
                    view.rootPhoneVerify,
                    activity as AppCompatActivity))
            {
                GlobalHelper.hideSoftKeyBoard(activity as AppCompatActivity,view!!.rootPhoneVerify)
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter?.sendOtp(view.countryCode_phoneVerify.selectedCountryCode,view.phoneNo_phoneVerify.text.toString().trim())
            }

        }

        Home.homeBackIcon.setOnClickListener {
            GlobalHelper.hideSoftKeyBoard(activity as AppCompatActivity,view.rootPhoneVerify)
            activity?.let { it.onBackPressed() }
        }

    }




    override fun onSendOtpSuccess(phoneVerify: SendOtp) {

        try {


            Toast.makeText(activity, "OTP: ${phoneVerify.body}",Toast.LENGTH_SHORT).show()

        GlobalHelper.hideProgress()
        phoneVerify.message.let { GlobalHelper.snackBar(view!!.rootPhoneVerify, it) }

        val loginData = LoginData()
        loginData.phone = view!!.phoneNo_phoneVerify.text.toString().trim()
        loginData.c_code = view!!.countryCode_phoneVerify.selectedCountryCode
        loginData.code = phoneVerify.body.toString()
        val bundle  = Bundle()
        bundle.putParcelable(Constants.LOGIN_DATA,loginData)
        val frag = OtpScreen()
        frag.arguments = bundle
        GlobalHelper.replaceFramentAnim(activity as AppCompatActivity,frag,R.id.container_map)
        }catch (ex : Exception){}

    }

    override fun onFailure(error: String) {
        GlobalHelper.hideProgress()
        error.let { GlobalHelper.snackBar(view!!.rootPhoneVerify, it) }
    }


}
