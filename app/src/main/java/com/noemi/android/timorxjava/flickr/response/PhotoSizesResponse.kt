package com.noemi.android.timorxjava.flickr.response

import com.google.gson.annotations.SerializedName
import com.noemi.android.timorxjava.flickr.model_api.FlickrSizes

data class PhotoSizesResponse(
    @SerializedName("sizes") val seizes: FlickrSizes
)