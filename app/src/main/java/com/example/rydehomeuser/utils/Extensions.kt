package com.example.rydehomeuser.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.rydehomeuser.utils.GlobalHelper


fun String.conCat(view: View, txtview: TextView, amountBalance: Boolean = true) {
    if (amountBalance) txtview.text = "${GlobalHelper.pound} $this" else txtview.text = "-${GlobalHelper.pound} $this"
}


fun String.conCatReturn(amountBalance: Boolean = true): String {
    return if (amountBalance) "${GlobalHelper.pound} $this" else "-${GlobalHelper.pound} $this"
}


//-----------------check this with null and empty value----------------------
/*fun String.conCatReturn(amountBalance: Boolean = true) :String {
    if (this == null) return ""
    return if (amountBalance)  "${GlobalHelper.pound} $this" else "-${GlobalHelper.pound} $this"
}*/


fun Context.showToast(context: Context, msg: String, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, msg, duration)
}


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun String.upperCaseFirstLetter(): String {
    return this.substring(0, 1).toUpperCase().plus(this.substring(1))
}


/*inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

sharedPreferences.edit {
    putString("name", "Rahul")
    putBoolean("isRegistered", true)
}*/






