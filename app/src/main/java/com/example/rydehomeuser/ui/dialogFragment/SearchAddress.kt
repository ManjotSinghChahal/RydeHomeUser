package com.example.rydehomeuser.ui.dialogFragment

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rydehomeuser.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class SearchAddress : BottomSheetDialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT)
        val view  = inflater.inflate(R.layout.search_address,container,false)



       /* view.lin_confirm_pickup.setOnClickListener {
            dismiss()
        }

        view.lin_cancel_pickup.setOnClickListener {
            dismiss()
        }*/

        return view


    }
}