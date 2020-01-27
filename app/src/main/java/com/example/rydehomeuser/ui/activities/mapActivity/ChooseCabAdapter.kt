package com.example.rydehomeuser.ui.activities.mapActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.model.getCarTypes.GetCarTypes
import com.example.rydehomeuser.ui.activities.home.Home
import com.example.rydehomeuser.utils.GlobalHelper
import kotlinx.android.synthetic.main.choosecab_adapter.view.*
import java.util.*


class ChooseCabAdapter(
    val context: Context,
    val carTypes: GetCarTypes, var callback: SelCarType
) : RecyclerView.Adapter<ChooseCabAdapter.ViewHolder>() {

    var amount = ""
    var distance = ""
    var timer: Timer = Timer()
    var arrayOfCarView = arrayListOf<View>()
    var addTimer : Boolean = true   // to start timer once

    companion object {
        var viewSel: View? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        addTimer = true
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choosecab_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return carTypes.body.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(carTypes)


    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bindItems(carTypes: GetCarTypes) {
            itemView.txt_username_requestCab.text = carTypes.body.get(adapterPosition).type
            // itemView.price_requestCab.text = carTypes.body.get(adapterPosition).price_per_km.toString()

            Glide.with(context).load(carTypes.body.get(adapterPosition).image).into(itemView.img_vehicle_requestCab)
            arrayOfCarView.add(itemView.price_requestCab)

         //   Log.e("printNowOut", GlobalHelper.TOTAL_DISTANCE.toString())

            /*  if (GlobalHelper.TOTAL_DISTANCE!=0.0)
              {
                  try {
                   val totalAmount =   (carTypes.body.get(adapterPosition).price_per_km)*GlobalHelper.TOTAL_DISTANCE
                      amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())
                      distance = GlobalHelper.TOTAL_DISTANCE.toString()
                      itemView.price_requestCab.text = "${GlobalHelper.pound} ${GlobalHelper.uptoTwoDecimal(totalAmount.toString())}"
                  }catch (ex : Exception){
                      itemView.price_requestCab.text = carTypes.body.get(adapterPosition).price_per_km.toString()
                      amount = GlobalHelper.uptoTwoDecimal(carTypes.body.get(adapterPosition).price_per_km.toString())
                      distance = GlobalHelper.TOTAL_DISTANCE.toString()
                  }
              }
              else
              {

                  Handler().postDelayed(
                      {
                          Log.e("printNowIn",GlobalHelper.TOTAL_DISTANCE.toString())
                          try {
                              val totalAmount =   (carTypes.body.get(adapterPosition).price_per_km)*GlobalHelper.TOTAL_DISTANCE
                              amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())
                              distance = GlobalHelper.TOTAL_DISTANCE.toString()
                              itemView.price_requestCab.text = "${GlobalHelper.pound} ${GlobalHelper.uptoTwoDecimal(totalAmount.toString())}"
                          }catch (ex : Exception){
                              itemView.price_requestCab.text = carTypes.body.get(adapterPosition).price_per_km.toString()
                              amount = GlobalHelper.uptoTwoDecimal(carTypes.body.get(adapterPosition).price_per_km.toString())
                              distance = GlobalHelper.TOTAL_DISTANCE.toString()
                          }
                      },
                      3000 // value in milliseconds
                  )
              }
  */

            if (addTimer)
            {
                addTimer = false
                Handler().postDelayed({
                    runThread()
                }, 500)

            }





            itemView.rel_vehilce_chooseCab.setOnClickListener {


                if (viewSel != null) {

                    val animP = AnimationUtils.loadAnimation(context, R.anim.scale_out_tv)
                    viewSel!!.startAnimation(animP)
                    animP.setFillAfter(true)
                }

                viewSel = itemView.root

                val anim = AnimationUtils.loadAnimation(context, R.anim.scale_in_tv)
                itemView.root.startAnimation(anim)
                anim.setFillAfter(true)


                //  selCarPos= position
                try {
                    val totalAmount = (carTypes.body.get(adapterPosition).price_per_km) * GlobalHelper.TOTAL_DISTANCE * GlobalHelper.CURRENT_PRICE_SURGE
                    amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())
                } catch (ex: Exception) {
                    amount = GlobalHelper.uptoTwoDecimal(carTypes.body.get(adapterPosition).price_per_km.toString())
                }
                callback.onSelCarTypeSuccess(
                    carTypes.body.get(adapterPosition).seating_capicity,
                    carTypes.body.get(adapterPosition).id,
                    carTypes.body.get(adapterPosition).price_per_km.toString(),
                    amount,
                    distance
                )
            }


        }


        /* itemView.root.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
             if (hasFocus) {
                 Log.e("bbbbb","111111111")
                 // run scale animation and make it bigger
                 val anim = AnimationUtils.loadAnimation(context, R.anim.scale_in_tv)
                 itemView.root.startAnimation(anim)
                 anim.setFillAfter(true)
             } else {
                 Log.e("bbbbb","111111111")
                 // run scale animation and make it smaller
                 val anim = AnimationUtils.loadAnimation(context, R.anim.scale_out_tv)
                 itemView.root.startAnimation(anim)
                 anim.setFillAfter(true)
             }
         })*/
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }


    interface SelCarType {
        fun onSelCarTypeSuccess(seater: String, vehicle_id: String, price: String, amount: String, distance: String)
    }

    fun runThread() {
        // to schedule periodically when dis is 0
        try {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {


                    (context as AppCompatActivity).runOnUiThread(object : Runnable {
                        override fun run() {

                            if (GlobalHelper.TOTAL_DISTANCE != 0.0) {
                                try {


                                    if (arrayOfCarView != null && arrayOfCarView.size > 0) {

                                        for (i in 0..arrayOfCarView.size - 1)
                                        {
                                            val totalAmount = (carTypes.body.get(i).price_per_km) * GlobalHelper.TOTAL_DISTANCE * GlobalHelper.CURRENT_PRICE_SURGE
                                            amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())
                                            distance = GlobalHelper.TOTAL_DISTANCE.toString()
                                            arrayOfCarView.get(i).price_requestCab.text =
                                                "${GlobalHelper.pound} ${GlobalHelper.uptoTwoDecimal(totalAmount.toString())}"

                                        }
                                    }

                                } catch (ex: Exception) {
                                }

                                timer.cancel()
                            }


                            /* if (GlobalHelper.TOTAL_DISTANCE!=0.0)
                             {
                                 try {

                                     val totalAmount =   (carTypes.body.get(adapterPosition).price_per_km)*GlobalHelper.TOTAL_DISTANCE
                                     Log.e("printAmtAdpt",GlobalHelper.uptoTwoDecimal(totalAmount.toString()))
                                     amount = GlobalHelper.uptoTwoDecimal(totalAmount.toString())
                                     distance = GlobalHelper.TOTAL_DISTANCE.toString()
                                     itemView.price_requestCab.text = "${GlobalHelper.pound} ${GlobalHelper.uptoTwoDecimal(totalAmount.toString())}"
                                 }catch (ex : Exception){
                                     itemView.price_requestCab.text = carTypes.body.get(adapterPosition).price_per_km.toString()
                                     amount = GlobalHelper.uptoTwoDecimal(carTypes.body.get(adapterPosition).price_per_km.toString())
                                     distance = GlobalHelper.TOTAL_DISTANCE.toString()
                                 }

                                 timer.cancel()
                             }*/

                        }

                    })
                }


            }, 0, 3000)


        } catch (ex: Exception) {
        }
    }


}