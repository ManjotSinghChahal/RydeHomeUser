package com.example.rydehomeuser.ui.activities.signUp.fragments.bussinessId


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.register.Register
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.signUp.SignUp
import com.example.rydehomeuser.ui.activities.signUp.SignupContract
import com.example.rydehomeuser.ui.activities.signUp.SignupPresenterImp
import com.example.rydehomeuser.utils.Constants.LOGIN_DATA
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import kotlinx.android.synthetic.main.fragment_bussiness_id.view.*


class BussinessID : androidx.fragment.app.Fragment(), SignupContract.RegisterView {



    var data: LoginData? = null
    var presenter: SignupContract.SignupPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bussiness_id, container, false)


        initialize(view)
        clickListener(view)


        return view
    }

    fun initialize(view: View) {

        val bundle = arguments
        if (bundle != null && bundle.containsKey(LOGIN_DATA)) {
            data = bundle.getParcelable(LOGIN_DATA)
        }

        presenter = SignupPresenterImp(this)

    }

    fun clickListener(view: View) {
        SignUp.signUpBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }

        }
        view.txt_businessId_next.setOnClickListener {

           /* if (view.bussinessId_Company.text.toString().trim().isEmpty()) {
                GlobalHelper.snackBar(view!!.rootBusinessId, getString(R.string.enter_business_id))
            } else {*/
                GlobalHelper.showProgress(activity as AppCompatActivity)
                presenter?.register(data!!.c_code,data!!.phone,data!!.f_name,data!!.l_name,view.bussinessId_Company.text.toString().trim())

           // }

        }


    }

    override fun onRegisterSuccess(register: Register) {
        GlobalHelper.hideProgress()
        register.message.let { GlobalHelper.snackBar(view!!.rootBusinessId, it) }

        SharedPrefUtil.getInstance()?.setLogin(true)
        SharedPrefUtil.getInstance()?.saveAuthToken(register.body.authorization_key)
        ActivityCompat.finishAffinity(activity as AppCompatActivity)
        startActivity(Intent(activity, Home::class.java))
        (activity as AppCompatActivity).finish()
        (activity as AppCompatActivity).overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }


    override fun onFailure(error: String) {
        GlobalHelper.hideProgress()
        error.let { GlobalHelper.snackBar(view!!.rootBusinessId, it) }
    }




}
