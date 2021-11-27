package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrPhoto(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: String,
    @SerializedName("title") val title: String
)