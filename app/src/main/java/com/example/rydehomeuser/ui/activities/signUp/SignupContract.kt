package com.example.rydehomeuser.ui.activities.signUp

import com.example.rydehomeuser.data.model.fbModel.FbModel
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.model.register.Register
import com.example.rydehomeuser.data.saveData.saveFbModel.SaveFbModel
import com.example.rydehomeuser.ui.baseclasses.BaseContract

interface SignupContract : BaseContract
{


    interface SignupPresenter {
        fun phoneVerify(country_code : String, phoneNo: String,device_token : String,device_type : String)
        fun otpVerify(country_code : String, phoneNo: String,otp : String)
        fun register(c_code: String, phone : String, fname : String, lname: String, businessId : String)
        fun fbLogin(saveFbModel: SaveFbModel)

    }

    interface SignupInteractor {
        fun phoneVerify(country_code : String, phoneNo: String,device_token : String,device_type : String, callback: OnSignupCompleteListener)
        fun otpVerify(country_code : String, phoneNo: String,otp : String, callback: OnSignupCompleteListener)
        fun register(c_code: String, phone : String, fname : String, lname: String, businessId : String, callback: OnSignupCompleteListener)
        fun fbLogin(saveFbModel: SaveFbModel, callback: OnSignupCompleteListener)
    }


    interface OnSignupCompleteListener : BaseContract.BaseOnCompleteListener
    {
        fun onPhoneVerifySucces(phoneVerify: PhoneVerify)
        fun onOtpVerifySucces(otpVerified: OtpVerified)
        fun onRegisterSucces(register: Register)
        fun onFbLoginSucces(fbModel: FbModel)

    }

    interface PhoneVerifyView : BaseContract.BaseView
    {
        fun onPhoneVerifySuccess(phoneVerify: PhoneVerify)
    }

    interface RegisterView : BaseContract.BaseView
    {
        fun onRegisterSuccess(register: Register)
    }

    interface OtpVerifiedView : BaseContract.BaseView
    {
        fun onOtpSuccess(otpVerified: OtpVerified)
    }

    interface SocialLoginView : BaseContract.BaseView
    {
        fun onSocialLoginSuccess(fbModel: FbModel)
    }



}