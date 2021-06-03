package com.example.proma.models

import android.os.Parcel
import android.os.Parcelable

data class Card(
  val name: String = "",
  val createdBy:  String = "",
  val assignTo: ArrayList<String> = ArrayList(),
  val labelColor: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel){
        writeString(name)
        writeString(createdBy)
        writeStringList(assignTo)
        writeString(labelColor)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}