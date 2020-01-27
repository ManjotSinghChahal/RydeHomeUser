package com.example.rydehomeuser.ui.dialogFragment

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.saveData.cancelReason.CancelReason
import kotlinx.android.synthetic.main.cancellation_adapter.view.*
import kotlinx.android.synthetic.main.reason_cancellation_dialog.view.*

class CancellatonAdapter(val activity: Context, val cancelList: CancelReason,
                         val done_reasonCancellation: TextView,
                         val callback : CancelRideAdapter) : RecyclerView.Adapter<CancellatonAdapter.ViewHolder>()
{
    var reason : String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.cancellation_adapter,parent,false))
    }

    override fun getItemCount(): Int {
     return cancelList.body.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = cancelList.body.get(position).name
        holder.bindItems(value)

    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindItems(value : String)
        {
            itemView.txtview_reason.text = value
            itemView.checkBoxCancelRideAdpt.setOnClickListener {

                if (cancelList.body.get(adapterPosition).selStatus)
                    cancelList.body.get(adapterPosition).selStatus = false
                else
                    cancelList.body.get(adapterPosition).selStatus = true

            }

            done_reasonCancellation.setOnClickListener {

                for (i in 0..cancelList.body.size-1)
                {
                    if (reason.isEmpty())
                    {
                        if (cancelList.body.get(i).selStatus)
                            reason = cancelList.body.get(i).name
                    }
                    else
                    {
                        if (cancelList.body.get(i).selStatus)
                            reason =  "$reason,${cancelList.body.get(i).name}"
                    }


                }
                callback.OnCancelRideAdpt(reason)
            }

        }


    }

    interface CancelRideAdapter
    {
        fun OnCancelRideAdpt(reason : String)
    }

}