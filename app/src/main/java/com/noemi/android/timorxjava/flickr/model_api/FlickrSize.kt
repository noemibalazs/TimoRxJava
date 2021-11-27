package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrSize(
    @SerializedName("label") val label: String,
    @SerializedName("source") val source: String
)