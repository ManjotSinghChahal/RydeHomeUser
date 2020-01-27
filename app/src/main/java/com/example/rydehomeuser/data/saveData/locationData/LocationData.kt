package com.example.rydehomeuser.data.saveData.locationData

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class LocationData : Parcelable
{

    var source_lat : String = ""
    var source_lng : String = ""
    var source_loc : String = ""
    var dest_lat : String = ""
    var dest_lng : String = ""
    var dest_loc : String = ""
    var stop_lat : String = ""
    var stop_lng : String = ""
    var stop_loc : String = ""
    var payment_mode : String = "1"
    var est_price : String = ""
    var vehilce_id : String = ""
    var booking_date : String = ""
    var add_stop : String = ""
    var time : String = ""
    var amount : String = ""
    var distance : String = ""
    var ride_id : String = ""
    var duration : String = ""

}