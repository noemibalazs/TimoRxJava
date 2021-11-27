package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrURL(
    @SerializedName("_content") val url: String
)