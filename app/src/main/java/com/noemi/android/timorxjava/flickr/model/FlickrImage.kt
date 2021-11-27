package com.noemi.android.timorxjava.flickr.model

import com.noemi.android.timorxjava.flickr.model_api.FlickrPhotoInfo
import com.noemi.android.timorxjava.flickr.model_api.FlickrSizes

data class FlickrImage(
    val id: String,
    val title: String,
    val owner: String,
    val thumbnail: String
) {
    companion object {

        fun createImage(info: FlickrPhotoInfo, sizes: FlickrSizes): FlickrImage {
            val flickrSize = sizes.size.first { it.label == "Medium" }
            return FlickrImage(
                id = info.id,
                owner = info.owner.userName,
                title = info.title.title,
                thumbnail = flickrSize.source
            )
        }
    }
}