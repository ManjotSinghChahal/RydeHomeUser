package com.example.rydehomeuser.ui.activities.signUp.fragments.otp


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.SignupContract
import com.example.rydehomeuser.ui.activities.signUp.SignupPresenterImp
import com.example.rydehomeuser.ui.activities.signUp.fragments.enterName.EnterName
import com.example.rydehomeuser.ui.activities.signUp.fragments.mobileNumber.MobileNumber
import com.example.rydehomeuser.utils.Constants.LOGIN_DATA
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_choose_type.view.*
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_otp.view.*
import java.lang.Exception


class Otp : androidx.fragment.app.Fragment(), SignupContract.OtpVerifiedView {


    var data: LoginData? = null
    var presenter: SignupContract.SignupPresenter? = null
    var phone: String = ""
    var c_code: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_otp, container, false)


        initialize(view)
        clickListener(view)


        return view
    }

    fun initialize(view: View) {
        presenter = SignupPresenterImp(this)

        SignUp.signUpBackIcon.visibility = View.VISIBLE
        SignUp.signUpCrossIcon.visibility = View.GONE
        SignUp.title_signUp.visibility = View.VISIBLE
        SignUp.title_signUp.text = getString(R.string.txt_verify_mobile)

        val bundle = arguments
        if (bundle != null && bundle.containsKey(LOGIN_DATA)) {
            data = bundle.getParcelable(LOGIN_DATA)
            phone = data!!.phone
            c_code = data!!.c_code

            Toast.makeText(activity,"Please enter OTP: ${data!!.code}",Toast.LENGTH_SHORT).show()

            view.txtview_info_otp.text =
                "${activity?.getString(R.string.inst_verify_mobile)} ${data!!.c_code} ${data!!.phone}"
        }

    }


    fun clickListener(view: View) {


        SignUp.signUpBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        view.txt_otp_submit.setOnClickListener {


            Log.e("printCode", data!!.code)

            if (view.txt_pin_entry.text.toString().trim().isEmpty()) {
                GlobalHelper.snackBar(view.rootOtp, getString(R.string.txt_enter_otp))
            } else if (view.txt_pin_entry.text.toString().trim().length < 4) {
                GlobalHelper.snackBar(view.rootOtp, getString(R.string.txt_enter_otp))
            } else {
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter!!.otpVerify(c_code, phone, view.txt_pin_entry.text.toString().trim())
            }


            /*else if (view.txt_pin_entry.text.toString().trim().equals(data!!.code))
            {
                val bundle  = Bundle()
                bundle?.putParcelable(LOGIN_DATA,data)
                val frag = EnterName()
                frag.arguments = bundle
                GlobalHelper.replaceFragment(activity as AppCompatActivity, frag,R.id.container_signup)
            }
            else
            {
                GlobalHelper.snackBar(view.rootOtp,getString(R.string.otp_not_match))
            }*/


        }


/*        view.edt_otp1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (view.edt_otp1.hasFocus()) {
                    view.edt_otp2.requestFocus()
                    view.img_pinview1.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        view.edt_otp2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (view.edt_otp2.hasFocus()) {
                    view.edt_otp3.requestFocus()
                    view.img_pinview2.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        view.edt_otp3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (view.edt_otp3.hasFocus()) {
                    view.edt_otp4.requestFocus()
                    view.img_pinview3.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        view.edt_otp4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (view.edt_otp4.hasFocus()) {
                    view.edt_otp4.requestFocus()
                    view.img_pinview4.visibility = View.GONE


                }


            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })*/


/*

            view.edt_otp1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //Perform Code
                    Log.e("vvcccc","11111111")

                    if (view.edt_otp1.hasFocus())
                    {
                        view.edt_otp1.setText("")
                        view.img_pinview1.visibility = View.VISIBLE
                        view.edt_otp1.requestFocus()
                    }


                }
                false
            })



            view.edt_otp2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //Perform Code
                    Log.e("vvcccc","2222222222")


                    if (view.edt_otp2.hasFocus())
                    {
                        view.edt_otp2.setText("")
                        view.img_pinview2.visibility = View.VISIBLE
                        view.edt_otp1.requestFocus()
                    }


                }
                false
            })


            view.edt_otp3.setOnKeyListener(View.OnKeyListener { v, keyCode3, event ->

                if (keyCode3 == KeyEvent.KEYCODE_DEL) {
                    //Perform Code
                    Log.e("vvcccc","33333333333")


                    if (view.edt_otp3.hasFocus())
                    {
                        view.edt_otp3.setText("")
                        view.img_pinview3.visibility = View.VISIBLE
                        view.edt_otp2.requestFocus()
                    }


                }
                false
            })


            view.edt_otp4.setOnKeyListener(View.OnKeyListener { v, keyCode4, event ->

                if (keyCode4 == KeyEvent.KEYCODE_DEL) {
                    //Perform Code
                    Log.e("vvcccc","444444444444444444")


                    if (view.edt_otp4.hasFocus())
                    {
                        view.edt_otp4.setText("")
                        view.img_pinview4.visibility = View.VISIBLE
                        view.edt_otp3.requestFocus()
                    }

                }
                false
            })
*/


    }

    override fun onOtpSuccess(otpVerified: OtpVerified) {

        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootOtp, otpVerified.message)
            val bundle  = Bundle()
            bundle?.putParcelable(LOGIN_DATA,data)
            val frag = EnterName()
            frag.arguments = bundle
            GlobalHelper.replaceFragment(activity as AppCompatActivity, frag,R.id.container_signup)
        } catch (ex: Exception) {
        }
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootOtp, error)
        } catch (ex: Exception) {
        }
    }


}
