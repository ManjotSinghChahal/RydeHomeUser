package com.example.rydehomeuser.data.saveData.loginData

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 class LoginData : Parcelable
{

    var phone: String = ""
    var c_code: String = ""
    var f_name: String = ""
    var l_name: String = ""
    var code: String = ""
}