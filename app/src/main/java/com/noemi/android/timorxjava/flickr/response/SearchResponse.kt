package com.noemi.android.timorxjava.flickr.response

import com.google.gson.annotations.SerializedName
import com.noemi.android.timorxjava.flickr.model_api.Photos

data class SearchResponse(
    @SerializedName("photos") val photos: Photos
)