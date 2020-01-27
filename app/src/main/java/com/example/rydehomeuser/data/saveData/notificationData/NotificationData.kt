package com.example.rydehomeuser.data.saveData.notificationData

import android.os.Parcel
import android.os.Parcelable

class NotificationData() : Parcelable
{

    var type : String = ""
    var title : String = ""
    var message : String = ""
    var driver_id : String = ""
    var source_lat : String = ""
    var source_long : String = ""
    var ride_id : String = ""
    var total_amount : String = ""
    var share_amount : String = ""
    var fareSplit_name : String = ""
    var fareSplit_photo : String = ""
    var dest_lat : String = ""
    var dest_long : String = ""

    constructor(parcel: Parcel) : this() {
        type = parcel.readString()
        title = parcel.readString()
        message = parcel.readString()
        driver_id = parcel.readString()
        source_lat = parcel.readString()
        source_long = parcel.readString()
        ride_id = parcel.readString()
        total_amount = parcel.readString()
        share_amount = parcel.readString()
        fareSplit_name = parcel.readString()
        fareSplit_photo = parcel.readString()
        dest_lat = parcel.readString()
        dest_long = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(message)
        parcel.writeString(driver_id)
        parcel.writeString(source_lat)
        parcel.writeString(source_long)
        parcel.writeString(ride_id)
        parcel.writeString(total_amount)
        parcel.writeString(share_amount)
        parcel.writeString(fareSplit_name)
        parcel.writeString(fareSplit_photo)
        parcel.writeString(dest_lat)
        parcel.writeString(dest_long)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotificationData> {
        override fun createFromParcel(parcel: Parcel): NotificationData {
            return NotificationData(parcel)
        }

        override fun newArray(size: Int): Array<NotificationData?> {
            return arrayOfNulls(size)
        }
    }


}