package com.example.rydehomeuser.ui.activities.signUp.fragments.mobileNumber


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.fragment.homeFragment.HomeFragment
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.SignupContract
import com.example.rydehomeuser.ui.activities.signUp.SignupPresenterImp
import com.example.rydehomeuser.ui.activities.signUp.fragments.enterName.EnterName
import com.example.rydehomeuser.ui.activities.signUp.fragments.otp.Otp
import com.example.rydehomeuser.utils.Constants.LOGIN_DATA
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import com.example.rydehomeuser.utils.Validator
import kotlinx.android.synthetic.main.fragment_mobile_number.view.*


class MobileNumber : Fragment(), SignupContract.PhoneVerifyView {


    var presenter: SignupContract.SignupPresenter? = null
    var phoneVal = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_mobile_number, container, false)


        initialize(view)
        clickListener(view)


        return view
    }


    fun initialize(view: View) {
        SignUp.signUpBackIcon.visibility = View.GONE
        SignUp.signUpCrossIcon.visibility = View.VISIBLE
        SignUp.title_signUp.visibility = View.VISIBLE
        SignUp.title_signUp.text = getString(R.string.mobilenum)


        presenter = SignupPresenterImp(this)

    }


    fun clickListener(view: View) {
        SignUp.signUpCrossIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        view.next_mobileNum.setOnClickListener {

            phoneVal =
                "${view.phoneNo_phoneVerify.text.toString().trim()}"

            if (Validator.getsInstance().phoneValidator(
                    view.phoneNo_phoneVerify.text.toString().trim(),
                    view.rootPhoneVerify,
                    activity as AppCompatActivity
                )
            ) {
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter?.phoneVerify(view.countryCode_phoneVerify.selectedCountryCode,phoneVal,SharedPrefUtil.getInstance()!!.getDeviceToken().toString(),"1")
            }

        }

    }


    override fun onPhoneVerifySuccess(phoneVerify: PhoneVerify) {

        try {
            GlobalHelper.hideProgress()
            val loginData = LoginData()
            loginData.phone = view!!.phoneNo_phoneVerify.text.toString().trim()
            loginData.c_code = view!!.countryCode_phoneVerify.selectedCountryCode
            loginData.code = phoneVerify.body.otp.toString()
            val bundle = Bundle()
            bundle.putParcelable(LOGIN_DATA, loginData)

            SharedPrefUtil.getInstance()!!.saveAuthToken(phoneVerify.body.authorization_key)




            if (phoneVerify.body.phone_verfied.equals("0") && phoneVerify.body.content.equals("0"))
            {
                val frag = Otp()
                frag.arguments = bundle
                GlobalHelper.replaceFragment(activity as AppCompatActivity, frag, R.id.container_signup)

                phoneVerify.message.let { GlobalHelper.snackBar(view!!.rootPhoneVerify, it) }
            }
            else if (phoneVerify.body.phone_verfied.equals("1") && phoneVerify.body.content.equals("0"))
            {
                val bundle  = Bundle()
                bundle?.putParcelable(LOGIN_DATA,loginData)
                val frag = EnterName()
                frag.arguments = bundle
                GlobalHelper.replaceFragment(activity as AppCompatActivity, frag, R.id.container_signup)
            }
            else  if (phoneVerify.body.phone_verfied.equals("1") && phoneVerify.body.content.equals("1"))
            {
              SharedPrefUtil.getInstance()?.setLogin(true)
              SharedPrefUtil.getInstance()!!.saveUserId(phoneVerify.body.id)

                activity!!.finishAffinity()
                startActivity(Intent(activity, Home::class.java))
                activity!!.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            }



        } catch (ex: Exception) {
        }

    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            error.let { GlobalHelper.snackBar(view!!.rootPhoneVerify, it) }
        } catch (ex: Exception) {
        }
    }


}
