package com.example.rydehomeuser.ui.activities.signUp

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.fbModel.FbModel
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.model.register.Register
import com.example.rydehomeuser.data.saveData.saveFbModel.SaveFbModel
import com.example.rydehomeuser.ui.baseclasses.App


class SignupPresenterImp() : SignupContract.SignupPresenter, SignupContract.OnSignupCompleteListener {



    lateinit var interactor: SignupContract.SignupInteractor
    var phoneVerifyView: SignupContract.PhoneVerifyView? = null
    var registerView: SignupContract.RegisterView? = null
    var otpVerifyView: SignupContract.OtpVerifiedView? = null
    var socialLoginView: SignupContract.SocialLoginView? = null

    init {

    }


    //------------------constructor for each view----------------------
    constructor(phoneVerify_view: SignupContract.PhoneVerifyView) : this() {
        interactor = SignupInteractorImp()
        phoneVerifyView = phoneVerify_view
    }

    constructor(register_view: SignupContract.RegisterView) : this() {
        interactor = SignupInteractorImp()
        registerView = register_view
    }

    constructor(otpVerify_view: SignupContract.OtpVerifiedView) : this() {
        interactor = SignupInteractorImp()
        otpVerifyView = otpVerify_view
    }

    constructor(socialLogin_view: SignupContract.SocialLoginView) : this() {
        interactor = SignupInteractorImp()
        socialLoginView = socialLogin_view
    }

    //--------------passing data to interactor----------------------

    override fun phoneVerify(c_code : String,phoneNo: String,device_token : String, device_type : String) {
        if (App.hasNetwork()) {
            interactor.phoneVerify(c_code,phoneNo,device_token, device_type,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun register(c_code: String, phone: String, fname: String, lname: String,businessID : String) {
        if (App.hasNetwork()) {
            interactor.register(c_code, phone,fname,lname,businessID,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }
    }

    override fun otpVerify(country_code: String, phoneNo: String, otp: String) {
        if (App.hasNetwork()) {
            interactor.otpVerify(country_code, phoneNo,otp,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }

    override fun fbLogin(saveFbModel: SaveFbModel) {
        if (App.hasNetwork()) {
            interactor.fbLogin(saveFbModel,this)
        } else {
            onFailure(App.instance!!.getString(R.string.no_internet))
        }    }







    //---------------------onApi-success-----------------------------

    override fun onPhoneVerifySucces(phoneVerify: PhoneVerify) {
      phoneVerify?.let { phoneVerifyView?.onPhoneVerifySuccess(phoneVerify) }
    }

    override fun onRegisterSucces(register: Register) {
        register?.let { registerView?.onRegisterSuccess(register) }
    }

    override fun onOtpVerifySucces(otpVerified: OtpVerified) {
        otpVerified?.let { otpVerifyView?.onOtpSuccess(it) }    }

    override fun onFbLoginSucces(fbModel: FbModel) {
        fbModel?.let { socialLoginView?.onSocialLoginSuccess(it) }     }



    //-----------------on api failure-----------------------
    override fun onFailure(error: String) {

       if(phoneVerifyView!=null)
            phoneVerifyView?.onFailure(error)
           else if(registerView!=null)
              registerView?.onFailure(error)
       else if(otpVerifyView!=null)
           otpVerifyView?.onFailure(error)
       else if(socialLoginView!=null)
           socialLoginView?.onFailure(error)
    }


}
