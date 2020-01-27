package com.example.rydehomeuser.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import java.util.*

class FbLogin(mContext: Context) {

    var mCallbackManager: CallbackManager
    internal var mCallback: FbLoginInterface


    interface FbLoginInterface {
        fun fbLoginResult(socialId: String, email: String, name: String, profilePic: String)
    }

    init {
        mCallbackManager = CallbackManager.Factory.create()
        mCallback = mContext as FbLoginInterface
        setFbCallManager(mContext)
    }

    fun fbLogin(mActivity: Activity) {
        LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("public_profile", "email", "user_birthday",
            "user_friends", "user_photos"))
    }

    internal fun setFbCallManager(mContext: Context) {
        //registerCallback from facebook
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
            //    CommonMethods.showProgress(mContext)
                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { `object`, response ->
                    // Application code
                    try {
                        val socialId = `object`.getString("id")
                        val firstName = `object`.getString("first_name")
                        val lastName = `object`.getString("last_name")
                        var email = `object`.optString("email")
                        if (email == "") {
                            email = `object`.getString("id") + "@gmail.com"
                        }
                        val fb_image = "https://graph.facebook.com/$socialId/picture?type=large"
                        LoginManager.getInstance().logOut()

                        mCallback.fbLoginResult(socialId, email, "$firstName $lastName", fb_image)

                    } catch (ex: JSONException) {
                        ex.printStackTrace()
                      //  CommonMethods.hideProgress()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                      //  CommonMethods.hideProgress()
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
                LoginManager.getInstance().logOut()
              //  CommonMethods.hideProgress()
            }

            override fun onError(exception: FacebookException) {
                LoginManager.getInstance().logOut()
              //  CommonMethods.hideProgress()
            }
        })
    }


}