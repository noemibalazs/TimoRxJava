package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrOwner(
    @SerializedName("username") val userName: String
)