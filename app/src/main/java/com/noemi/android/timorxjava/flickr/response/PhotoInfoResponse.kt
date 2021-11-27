package com.noemi.android.timorxjava.flickr.response

import com.google.gson.annotations.SerializedName
import com.noemi.android.timorxjava.flickr.model_api.FlickrPhotoInfo

data class PhotoInfoResponse(
    @SerializedName("photo") val photoInfo: FlickrPhotoInfo
)
