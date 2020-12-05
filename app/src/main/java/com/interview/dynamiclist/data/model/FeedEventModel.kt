package com.interview.dynamiclist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FeedEventModel(@SerializedName("name") @Expose val name: String,
                          @SerializedName("weight") @Expose val weight: String,
                          @SerializedName("bagColor") @Expose val color: String) : BaseEventModel()