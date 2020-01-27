package com.example.rydehomeuser.ui.activities.signUp

import android.util.Log
import com.app.passerby.data.injectorApi.InjectorApi
import com.app.passerby.data.injectorApi.InterfaceApi
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.fbModel.FbModel
import com.example.rydehomeuser.data.model.otpVerified.OtpVerified
import com.example.rydehomeuser.data.model.phoneVerify.PhoneVerify
import com.example.rydehomeuser.data.model.register.Register
import com.example.rydehomeuser.data.saveData.saveFbModel.SaveFbModel
import com.example.rydehomeuser.ui.baseclasses.App
import com.example.rydehomeuser.utils.Constants.DEVICE_TYPE
import com.example.rydehomeuser.utils.Constants.MESSAGE
import com.example.rydehomeuser.utils.GlobalHelper
import com.example.rydehomeuser.utils.SharedPrefUtil
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException


class SignupInteractorImp : SignupContract.SignupInteractor {


    var mApi: InterfaceApi

    init {
        this.mApi = InjectorApi.provideApi()
    }


    override fun phoneVerify(country_code : String, phoneNo: String,device_token : String,device_type : String, callback: SignupContract.OnSignupCompleteListener) {


        mApi.phoneVerify(country_code,phoneNo,SharedPrefUtil.getInstance()!!.getDeviceToken().toString(),DEVICE_TYPE).enqueue(object : retrofit2.Callback<PhoneVerify> {
            override fun onResponse(call: Call<PhoneVerify>, response: Response<PhoneVerify>) {



                if(response.isSuccessful)
                    response.body()?.let { callback.onPhoneVerifySucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()
                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<PhoneVerify>, t: Throwable) {
                callback.onFailure(t.message.toString())
            }
        })
    }


    override fun register(c_code: String, phone: String, fname: String, lname: String,businessId : String, callback: SignupContract.OnSignupCompleteListener) {

        mApi.register(c_code,phone,fname,lname,DEVICE_TYPE,SharedPrefUtil.getInstance()!!.getDeviceToken().toString(),businessId,GlobalHelper.CURRENT_LAT.toString(),GlobalHelper.CURRENT_LNG.toString()).enqueue(object : retrofit2.Callback<Register> {
            override fun onResponse(call: Call<Register>, response: Response<Register>) {




                if(response.isSuccessful)
                    response.body()?.let { callback.onRegisterSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()

                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<Register>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })
    }



    override fun otpVerify( country_code: String, phoneNo: String, otp: String, callback: SignupContract.OnSignupCompleteListener) {

        mApi.otpVerify("User",otp,"1",phoneNo,country_code).enqueue(object : retrofit2.Callback<OtpVerified> {
            override fun onResponse(call: Call<OtpVerified>, response: Response<OtpVerified>) {




                if(response.isSuccessful)
                    response.body()?.let { callback.onOtpVerifySucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()

                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<OtpVerified>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })

    }

    override fun fbLogin(saveFbModel: SaveFbModel, callback: SignupContract.OnSignupCompleteListener) {

        mApi.fblogin(saveFbModel).enqueue(object : retrofit2.Callback<FbModel> {
            override fun onResponse(call: Call<FbModel>, response: Response<FbModel>) {




                if(response.isSuccessful)
                    response.body()?.let { callback.onFbLoginSucces(it) }
                else
                {
                    try {
                        val body = response.errorBody()!!.string()

                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<FbModel>, t: Throwable) {
                callback.onFailure(t.message.toString())

            }
        })
    }




}





/*
    override fun login(email: String, password: String, callback: LoginContract.OnLoginCompleteListener) {

        Log.e("tokenPrintHere",SharedPrefUtil.getInstance()!!.getDeviceToken().toString())

        mApi.login(email,password,SharedPrefUtil.getInstance()!!.getDeviceToken().toString(),Constants.DEVICE_LAT,Constants.DEVICE_LNG).enqueue(object : retrofit2.Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {

                if(response.isSuccessful)
                    callback.onLoginSucces(response.body())
                else
                {
                    try {
                        val body = response.errorBody()!!.string()

                        val `object` = JSONObject(body)
                        val error = `object`.getString(MESSAGE)
                        callback.onFailure(error)
                    } catch (e: JSONException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    } catch (e: IOException) {
                        App.instance?.getString(R.string.some_error)?.let { callback.onFailure(it) }
                        e.printStackTrace()
                    }

                }

            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                callback.onFailure(t.message.toString())



            }
        })


    }*/
