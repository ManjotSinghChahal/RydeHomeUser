package com.example.rydehomeuser.ui.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.example.rydehomeuser.R
import kotlinx.android.synthetic.main.logout_dialog.view.*

class LogoutDialog(val callback : LogoutInterface) : DialogFragment()
{



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.logout_dialog,container,false)

        view.yes_logout.setOnClickListener {
         callback.onLogoutInterface()
            dismiss()
        }

        view.no_logout.setOnClickListener {
         dismiss()
        }

        return view

    }

    interface LogoutInterface
    {
        fun onLogoutInterface()
    }





}