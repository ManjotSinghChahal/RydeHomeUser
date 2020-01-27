package com.example.rydehomeuser.utils

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.rydehomeuser.R


class Validator {



    fun updateProfileValidator(fname: String, lname: String,phone : String,email : String,password : String, root: LinearLayout, context: Context): Boolean {
        if (fname.trim().isEmpty()) {
            context.getString(R.string.enter_f_name)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else  if (lname.trim().isEmpty()) {
            context.getString(R.string.enter_l_name)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
       /* else  if (phone.trim().isEmpty()) {
            context.getString(R.string.enter_mobilenum)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else  if (phone.trim().length<6  || phone.trim().length>15) {
            context.getString(R.string.enter_valid_mobilenum)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }*/
        else  if (email.trim().isEmpty()) {
            context.getString(R.string.enter_email)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            context.getString(R.string.invalid_email)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (password.trim ().isEmpty()) {
            context.getString(R.string.enter_passsword)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (password.trim ().length<8) {
            context.getString(R.string.password_six_chars)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }

        return true
    }

    fun phoneValidator(phone: String, root: View, context: Context): Boolean {
        if (phone.trim().isEmpty()) {
            context.getString(R.string.phone_empty)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (phone.length<6 || phone.length>15) {
            context.getString(R.string.phone_invalid)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        return true
    }

/*    fun passwordValidator(password: String, root: View, context: Context): Boolean {
        if (password.trim ().isEmpty()) {
            context.getString(R.string.password_empty)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (password.trim ().length<6) {
            context.getString(R.string.password_invalid)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }


        return true
    }
    fun nameValidator(name: String, root: View, context: Context): Boolean {
        if (name.trim ().isEmpty()) {
            context.getString(R.string.name_empty)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        else if (name.trim ().length<3) {
            context.getString(R.string.name_invalid)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        return true
    }

    fun otpValidator(otpLength: Int, root: View, context: Context): Boolean {
        if (otpLength<4) {
            context.getString(R.string.enter_otp_full)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        return true
    }

    fun bdayValidator(bday: String, root: View, context: Context): Boolean {
        if (bday.isEmpty()) {
            context.getString(R.string.select_dob)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        return true
    }



    fun imageValidator(image: String, root: View, context: Context): Boolean {
        if (image.trim ().isEmpty()) {
            context.getString(R.string.image_empty)?.let { GlobalHelper.snackBar(root, it) }
            return false
        }
        return true
    }*/




    companion object {
        private var sInstance: Validator? = null

        fun getsInstance(): Validator {
            if (sInstance == null) {
                sInstance = Validator()
            }
            return sInstance as Validator
        }
    }
}
