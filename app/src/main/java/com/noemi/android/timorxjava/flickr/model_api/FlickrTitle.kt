package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrTitle(
    @SerializedName("_content") val title: String
)