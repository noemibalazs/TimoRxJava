package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrSizes(
    @SerializedName("size") val size: MutableList<FlickrSize>
)