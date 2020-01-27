package com.example.rydehomeuser.ui.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ContactModel(val name: String, val contact: String) : Parcelable
/*{



    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ContactModel> {
            override fun createFromParcel(parcel: Parcel) = ContactModel(parcel)
            override fun newArray(size: Int) = arrayOfNulls<ContactModel>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        name = parcel.readString(),
        contact = parcel.readString()

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(contact)

    }

    override fun describeContents() = 0
}*/