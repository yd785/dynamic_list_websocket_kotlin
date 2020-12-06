package com.interview.dynamiclist.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedEventModel(@SerializedName("name") @Expose val name: String = "",
                          @SerializedName("weight") @Expose val weight: String = "",
                          @SerializedName("bagColor") @Expose val color: String = "") : BaseEventModel(),
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(weight)
        parcel.writeString(color)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeedEventModel> {
        override fun createFromParcel(parcel: Parcel): FeedEventModel {
            return FeedEventModel(parcel)
        }

        override fun newArray(size: Int): Array<FeedEventModel?> {
            return arrayOfNulls(size)
        }
    }

}