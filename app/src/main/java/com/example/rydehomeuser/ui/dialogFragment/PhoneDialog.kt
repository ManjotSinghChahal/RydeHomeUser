package com.example.rydehomeuser.ui.dialogFragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.rydehomeuser.R
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.phone_dialog.view.*


class PhoneDialog(val phone : String, context : Context) : DialogFragment()
{



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.phone_dialog,container,false)


        view.phoneNumber_phoneDialog.text = phone

        view.call_phoneDialog.setOnClickListener {
            val intent = Intent("android.intent.action.CALL")
            val data = Uri.parse("tel:$phone")
            intent.data = data
            startActivity(intent)

            dismiss()
        }

        checkPermissionForCall(context!!)

        view.cancel_phoneDialog.setOnClickListener {
            dismiss()
        }



        return view

    }


    fun checkPermissionForCall(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE),
                    1
                )
                false
            }
        } else {
            true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }





}
