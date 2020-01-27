package com.example.rydehomeuser.ui.activities.home.fragment.loyalty


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.rydehomeuser.R
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.fragment.wallet.Wallet
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_loyalty.view.*


class Loyalty : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_loyalty, container, false)


        GlobalHelper.setToolbar(getString(R.string.loyalty),homeCrossIconVis = true)
        clickListener(view)

        return view
    }



    fun clickListener(view : View)
    {

        Home.homeCrossIcon.setOnClickListener {
            activity?.onBackPressed()
        }

        view.rel_wallet.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.container_map,Wallet()).addToBackStack(null).commit()

        }
    }


}
