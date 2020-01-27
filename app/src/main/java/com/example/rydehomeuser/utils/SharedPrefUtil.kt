package com.example.rydehomeuser.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.rydehomeuser.ui.baseclasses.App

class SharedPrefUtil(private val context: Context) {


    companion object {

        val NAME = "name"
        val PHONE_NUMBER = "phoneNumber"
        val EMAIL = "email"
        val IMAGE = "profileImage"
        val LOGIN_TYPE = "loginType"
        val USER_ID = "userId"
        val USER_Name = "userName"
        val PASSWORD = "password"
        val DEVICE_TOKEN = "deviceToken"
        val AUTH_TOKEN = "authToken"
        val LOGIN = "login"
        val DEVICE_TKEN = "dev_token"


        /**
        * Name of the preference file
        */
        private val APP_PREFS = "application_preferences"
        private var mSharedPreferences: SharedPreferences? = null
        private var instance: SharedPrefUtil? = null

        fun init(context: Context) {
            if (instance == null) {
                instance = SharedPrefUtil(context)
            }
        }

        fun getInstance(): SharedPrefUtil? {
            if (instance == null) {
                instance = App.instance?.let { SharedPrefUtil(it) }
            }
            return instance
        }


    }

    private var mEditor: SharedPreferences.Editor? = null
    private val mContext: Context





   init {
       this.mContext = context
       mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
   }



    /**
     * Save a string into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    fun saveString(key: String, value: String) {

        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(key, value)
        mEditor?.apply()
    }

    /**
     * Save a int into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    fun saveInt(key: String, value: Int) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putInt(key, value)
        mEditor?.apply()
    }

    /**
     * Save a boolean into shared preference
     *
     * @param key   The name of the preference to modify
     * @param value The new value for the preference
     */
    fun saveBoolean(key: String, value: Boolean) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putBoolean(key, value)
        mEditor?.apply()
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or null.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    fun getString(key: String): String? {
        //    mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences?.getString(key, "")
    }

    /**
     * Retrieve a String value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a String.
     */
    fun getString(key: String, defaultValue: String): String? {
        //    mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences?.getString(key, defaultValue)
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or 0.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    fun getInt(key: String): Int? {
        // mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences?.getInt(key, 0)
    }

    /**
     * Retrieve a int value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a int.
     */
    fun getInt(key: String, defaultValue: Int): Int? {
        //     mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences?.getInt(key, defaultValue)
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @return Returns the preference value if it exists, or false.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    fun getBoolean(key: String): Boolean? {
        //     mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        return mSharedPreferences?.getBoolean(key, false)
    }

    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue.
     * Throws ClassCastException if there is a preference with this name that is not a boolean.
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getBoolean(key, defaultValue)
    }


    /**
     * save the device token.
     *
     * @param token Vslue retrieved from the firebase.
     */
    fun saveDeviceToken(token: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(DEVICE_TOKEN, token)
        mEditor?.apply()
    }


    /**
     * get the current device token
     *
     * @return Returns the last update device token got from firebase.
     */
    fun getDeviceToken(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(DEVICE_TOKEN, "")
    }
//

    /**
     * save the userId of current user using the application
     *
     * @param userId this is the userId of the user
     */
    fun saveUserId(userId: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(USER_ID, userId)
        mEditor?.apply()
    }

    fun saveUserName(name: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(USER_Name, name)
        mEditor?.apply()
    }

    /**
     * get current login user's userId .
     *
     * @return Returns the last userId saved.
     */
    fun getUserId(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(USER_ID, "")
    }

    fun getUserName(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(USER_Name, "")
    }

    /**
     * save the latest authorization token of the user
     *
     * @param authToken authoriztion token of the user.
     */
    fun saveAuthToken(authToken: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(AUTH_TOKEN, authToken)
        mEditor?.apply()
    }

    /**
     * get the authorization of the user
     */
    fun getAuthToken(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(AUTH_TOKEN, "")
    }


    /**
     * set login state oin the device is there any user current login device
     */
    fun setLogin(value: Boolean) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putBoolean(LOGIN, value)
        mEditor?.apply()
    }

    fun setDeviceTken(value: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(DEVICE_TKEN, value)
        mEditor?.apply()
    }

    /**
     * @return is anyuser current login into the device accordingly true/false.
     */
    fun isLogin(): Boolean? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getBoolean(LOGIN, false)
    }


    /**
     * save name of the user
     */
    fun saveName(name: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(NAME, name)
        mEditor?.apply()
    }

    /**
     * get name of the user
     */
    fun getName(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(NAME, "")
    }


    /**
     * save email of the user
     */
    fun saveEmail(email: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(EMAIL, email)
        mEditor?.apply()
    }

    /**
     * get email of the user
     */
    fun getEmail(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(EMAIL, "")
    }

    /**
     * save phoneNumber of the user
     */
    fun savePhoneNumber(phoneNumber: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(PHONE_NUMBER, phoneNumber)
        mEditor?.apply()
    }

    /**
     * get phoneNumber of the user
     */
    fun getPhoneNumber(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(PHONE_NUMBER, "")
    }

    /**
     * save image of the user
     */
    fun saveImage(image: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(IMAGE, image)
        mEditor?.apply()
    }


    fun getLoginType(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences?.getString(LOGIN_TYPE, "")
    }

    fun saveLoginType(image: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(LOGIN_TYPE, image)
        mEditor?.apply()
    }

    /**
     * get image of the user
     */
    fun getImage(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(IMAGE, "")
    }

    fun savePassword(password: String) {
        mEditor = mSharedPreferences?.edit()
        mEditor?.putString(PASSWORD, password)
        mEditor?.apply()
    }

    fun getPassword(): String? {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        return mSharedPreferences!!.getString(PASSWORD, "")
    }

    /**
     * Clears the shared preference file
     */
    fun clear() {
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)
        mSharedPreferences?.let {
          //  it.edit().clear().apply()
            mEditor = mSharedPreferences?.edit()
            SharedPrefUtil.getInstance()?.saveEmail("")
            SharedPrefUtil.getInstance()?.saveAuthToken("")
            SharedPrefUtil.getInstance()?.saveUserId("")
            SharedPrefUtil.getInstance()?.saveUserName("")
            SharedPrefUtil.getInstance()?.saveImage("")
            mEditor!!.apply()



        }
    }


    /**
     * save user all data when user user login into the device
     *
     * @param userModel
     */
/*    fun saveLoginData(userModel: UserModel) {
        mEditor = mSharedPreferences.edit()
        mEditor.putString(USER_ID, String.valueOf(userModel.getId()))
        mEditor.putString(AUTH_TOKEN, userModel.getAuthorizationKey())
        mEditor.putString(NAME, userModel.getUsername())
        mEditor.putString(EMAIL, userModel.getEmail())
        mEditor.putString(IMAGE, userModel.getImage())
        mEditor.putString(LOGIN_TYPE, NORMAL)
        mEditor.putString(PHONE_NUMBER, userModel.getMobile())
        mEditor.apply()
    }*/

    /**
     * save user all data when user user login into the device through social login
     *
     * @param userModel
     */
/*    fun saveSocialLoginData(userModel: UserModel) {
        mEditor = mSharedPreferences.edit()
        mEditor.putString(LOGIN_TYPE, SOCIAL)
        mEditor.putString(USER_ID, String.valueOf(userModel.getId()))
        mEditor.putString(AUTH_TOKEN, userModel.getAuthorizationKey())
        mEditor.putString(NAME, userModel.getName())
        mEditor.putString(EMAIL, userModel.getEmail())
        mEditor.putString(IMAGE, userModel.getImage())
        mEditor.putString(PHONE_NUMBER, userModel.getMobile())
        mEditor.apply()
    }*/

    /**
     * clear all user's save data whenever user logout the device.
     */
    fun onLogout() {
        mEditor = mSharedPreferences?.edit()
        if (getPassword() != "") {
            mEditor?.apply {
                remove(USER_ID)
                remove(AUTH_TOKEN)
                remove(EMAIL)
                remove(IMAGE)
                remove(PHONE_NUMBER)
            }
        } else {
            mEditor?.apply {
                remove(USER_ID)
                remove(AUTH_TOKEN)
                remove(NAME)
                remove(EMAIL)
                remove(IMAGE)
                remove(PHONE_NUMBER)
            }
        }
        setLogin(false)
        mEditor?.apply()
    }







}



