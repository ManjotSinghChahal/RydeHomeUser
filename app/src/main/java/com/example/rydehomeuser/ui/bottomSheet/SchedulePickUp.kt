package com.example.rydehomeuser.ui.bottomSheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rydehomeuser.R
import com.example.rydehomeuser.data.saveData.locationData.LocationData
import com.example.rydehomeuser.utils.GlobalHelper.formatDate
import com.example.rydehomeuser.utils.GlobalHelper.getDatePicker
import com.example.rydehomeuser.utils.GlobalHelper.getTimePicker
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.schedule_pickup.*
import kotlinx.android.synthetic.main.schedule_pickup.view.*
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS


class SchedulePickUp(val locData : LocationData,val callback : ScheduleCabInterface) : BottomSheetDialogFragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view  = inflater.inflate(R.layout.schedule_pickup,container,false)



        view.lin_confirm_pickup.setOnClickListener {

            if (view.selectDate_schedulePickup.text.toString().trim().equals(getString(R.string.select_date)))
                Toast.makeText(activity as AppCompatActivity,getString(R.string.please_select_date),Toast.LENGTH_SHORT).show()
            else if (view.selectTime_schedulePickup.text.toString().trim().equals(getString(R.string.select_time)))
                Toast.makeText(activity as AppCompatActivity,getString(R.string.please_select_time),Toast.LENGTH_SHORT).show()
            else
            {
                locData.time = view.selectTime_schedulePickup.text.toString().trim()
                locData.booking_date = formatDate(view.selectDate_schedulePickup.text.toString().trim())
                callback.OnScheduleCab(locData)
                dismiss()





            }

        }

        view.lin_cancel_pickup.setOnClickListener {
            dismiss()
        }

        view.lin_datePicker.setOnClickListener {
            getDatePicker(view.selectDate_schedulePickup,activity as AppCompatActivity)
        }

        view.lin_timePicker.setOnClickListener {
            getTimePicker(view.selectTime_schedulePickup,activity as AppCompatActivity)
        }

        return view


    }


    interface ScheduleCabInterface
    {
        fun OnScheduleCab(locDataNew : LocationData)
    }


}