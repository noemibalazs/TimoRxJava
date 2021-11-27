package com.noemi.android.timorxjava.flickr.model_api

import com.google.gson.annotations.SerializedName

data class FlickrPhotoInfo(
    @SerializedName("id") val id: String,
    @SerializedName("owner") val owner: FlickrOwner,
    @SerializedName("title") val title: FlickrTitle,
    @SerializedName("urls") val urls: FlickrURLs
)