package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class Photos(
    @SerializedName("photo") val photo: MutableList<FlickrPhoto>
)