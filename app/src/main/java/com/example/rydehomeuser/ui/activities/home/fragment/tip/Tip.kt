package com.example.rydehomeuser.ui.activities.home.fragment.tip


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment

import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.tipModel.TipModel
import com.example.rydehomeuser.data.saveData.locationData.LocationData
import com.example.rydehomeuser.data.saveData.loginData.LoginData
import com.example.rydehomeuser.ui.activities.getCards.GetCards
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.ui.activities.home.HomeContract
import com.example.rydehomeuser.ui.activities.home.HomePresenterImp
import com.example.rydehomeuser.utils.Constants.CARD_ID
import com.example.rydehomeuser.utils.Constants.CVV
import com.example.rydehomeuser.utils.Constants.TIP_TYPE
import com.example.rydehomeuser.utils.GlobalHelper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_tip.view.*
import org.jetbrains.anko.async


class Tip(val rideId : String,val driver_name : String) : DialogFragment(), HomeContract.TipView {


    lateinit var  chipGroup : ChipGroup
    var chip : Chip? = null
    var presenter: HomeContract.HomePresenter? = null
    val CARD_TYPE: Int = 4
    var card_id = ""
    var cvv_num = ""
    var ride_id = ""
    var amount = ""
    var driverName = ""

    init {
        ride_id =  rideId
        driverName =  driver_name
    }

    override fun getTheme(): Int {
        return R.style.FullScreenDialog
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tip, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlobalHelper.setToolbar(getString(R.string.tip),homeBackIconVis = true)
        clickListener(view)


        view.driverName_tip.text = "${getString(R.string.add_a_tip)} $driverName"
        chipGroup = view.findViewById(R.id.chipGroup_Tip) as ChipGroup
        addChip(" Tip Amount: 2 ")

        presenter = HomePresenterImp(this)

    }


    fun clickListener(view : View)
    {

        view.relBack_tip.setOnClickListener {
            backToHome()
        }

        view.rel_tipTwo_tip.setOnClickListener {
            view.edt_enterTip.setText("")
            chip!!.text = " Tip Amount: 2 "
            amount ="2"
        }

        view.rel_tipThree_tip.setOnClickListener {
            view.edt_enterTip.setText("")
            chip!!.text = " Tip Amount: 3 "
            amount ="3"
        }

        view.rel_tipFive_tip.setOnClickListener {
            view.edt_enterTip.setText("")
            chip!!.text = " Tip Amount: 5 "
            amount ="5"
        }

        view.done_Tip.setOnClickListener {


            if (card_id.isEmpty() && cvv_num.isEmpty())
            {
                val intent = Intent(activity, GetCards::class.java)
                intent.putExtra(TIP_TYPE, TIP_TYPE)
                startActivityForResult(intent, CARD_TYPE)
            }
            else
            {
                 GlobalHelper.showProgress(activity as AppCompatActivity)
                 presenter?.tip(ride_id,amount,card_id,cvv_num)
            }

        }



        view.edt_enterTip.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                chip!!.text = " Tip Amount: ${view.edt_enterTip.text.trim()} "
                amount ="${view.edt_enterTip.text.trim()}"
            }

        })

    }


    fun addChip(value: String)
    {
        chip = Chip(activity)
        chip!!.text = value
        chip!!.isCloseIconEnabled = false
        chip!!.textSize = 16.0f
        chip!!.setCloseIconTintResource(R.color.colorWhite)
        chip!!.setTextColor(resources.getColor(R.color.colorWhite))
        chip!!.chipBackgroundColor =
            AppCompatResources.getColorStateList(activity as AppCompatActivity, R.color.colorAppTheme)
            chipGroup.addView(chip)




        /*chip!!.setOnCloseIconClickListener {
            chipGroup.removeView(chip)
        }*/
    }


    override fun onTipSuccess(tipModel: TipModel) {
        try {
            GlobalHelper.hideProgress()
            if (tipModel.code.equals(200))
                backToHome()
        }catch (ex : Exception){}
    }

    override fun onFailure(error: String) {
        try {
            GlobalHelper.hideProgress()
        }catch (ex : Exception){}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data!=null)
        {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    //----for card select----------------------
                   /* if (requestCode == CARD_TYPE)
                    {*/
                        card_id = data!!.getStringExtra(CARD_ID)
                        cvv_num = data!!.getStringExtra(CVV)
                  //  }


                }catch (ex : Exception){

                }
            }
        }



    }


    fun backToHome()
    {
        activity?.let {
            it.finishAffinity()
            it.startActivity(Intent(it, Home::class.java))
            it.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }



}
