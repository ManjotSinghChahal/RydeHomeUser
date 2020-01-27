package com.example.rydehomeuser.ui.activities.home.fragment.wallet


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getWalletRewards.GetWalletRewards
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.ui.dialogFragment.PriceInformation
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.fragment_wallet.view.*


class Wallet : Fragment(), HomeContract.WalletRewardsView {
    var presenter: HomeContract.HomePresenter? = null
    var recyclerviewWallet : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = inflater.inflate(R.layout.fragment_wallet, container, false)

        initialize(view)
        clickListener(view)

        return view
    }


    fun clickListener(View : View)
    {

        Home.homeBackIcon.setOnClickListener {
            activity?.let { it.onBackPressed() }
        }

        Home.relInfoHome.setOnClickListener {
            val ft = activity!!.supportFragmentManager.beginTransaction()
            val priceInfo = PriceInformation()
            priceInfo.show(ft, "")
        }



    }

    fun initialize(view : View)
    {
        GlobalHelper.setToolbar(getString(R.string.wallet),homeBackIconVis = true, relHomeInfo = true)
        presenter = HomePresenterImp(this)
        GlobalHelper.showProgress(activity as AppCompatActivity)
        presenter!!.getWalletRewards()

        recyclerviewWallet = view.findViewById(R.id.recyclerview_wallet) as RecyclerView
        recyclerviewWallet!!.layoutManager = activity?.let { LinearLayoutManager(it) }

    }


    override fun onWalletRewardSuccess(getWalletRewards: GetWalletRewards) {

      try {
       GlobalHelper.hideProgress()
       if (getWalletRewards.code.equals(200))
       {
           if (!getWalletRewards.body.totalRewards.isEmpty()   )
             view!!.totalRewards_wallet.text = getWalletRewards.body.totalRewards

           activity?.let {
               recyclerviewWallet!!.adapter = WalletAdapter(it,getWalletRewards)
           }
       }
      }catch (ex : Exception){}

       }

    override fun onFailure(error: String) {

        try {
            GlobalHelper.hideProgress()
            GlobalHelper.snackBar(view!!.rootWallet,error)
        }catch (ex : Exception){}

    }


}
