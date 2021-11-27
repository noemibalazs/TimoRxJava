package com.noemi.android.timorxjava.flickr.remote_data

import com.noemi.android.timorxjava.flickr.api.FlickrAPI
import com.noemi.android.timorxjava.flickr.response.PhotoInfoResponse
import com.noemi.android.timorxjava.flickr.response.PhotoSizesResponse
import com.noemi.android.timorxjava.flickr.response.SearchResponse
import com.noemi.android.timorxjava.utils.Constants.FLICKR_KEY
import io.reactivex.Observable

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteAPISource @Inject constructor(private val api: FlickrAPI) : RemoteAPI {

    override fun searchPhotos(tags: String): Observable<SearchResponse> {
        return api.searchPhotos(FLICKR_KEY, tags)
    }

    override fun getPhotoInfo(photoId: String): Observable<PhotoInfoResponse> {
        return api.photoInfo(FLICKR_KEY, photoId)
    }

    override fun getPhotoSizes(photoId: String): Observable<PhotoSizesResponse> {
        return api.getSizes(FLICKR_KEY, photoId)
    }
}