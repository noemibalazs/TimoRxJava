package com.noemi.android.timorxjava.flickr.remote_data

import com.noemi.android.timorxjava.flickr.response.PhotoInfoResponse
import com.noemi.android.timorxjava.flickr.response.PhotoSizesResponse
import com.noemi.android.timorxjava.flickr.response.SearchResponse
import io.reactivex.Observable

interface RemoteAPI {

    fun searchPhotos(tags: String): Observable<SearchResponse>

    fun getPhotoInfo(photoId: String): Observable<PhotoInfoResponse>

    fun getPhotoSizes(photoId: String): Observable<PhotoSizesResponse>
}