package com.noemi.android.timorxjava.flickr.api

import com.noemi.android.timorxjava.flickr.response.PhotoInfoResponse
import com.noemi.android.timorxjava.flickr.response.PhotoSizesResponse
import com.noemi.android.timorxjava.flickr.response.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrAPI {

    @GET("/services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&per_page=12")
    fun searchPhotos(
        @Query("api_key") apiKey: String,
        @Query("tags") tags: String
    ): Observable<SearchResponse>

    @GET("/services/rest/?method=flickr.photos.getInfo&format=json&nojsoncallback=1")
    fun photoInfo(
        @Query("api_key") apiKey: String,
        @Query("photo_id") photoId: String
    ): Observable<PhotoInfoResponse>

    @GET("/services/rest/?method=flickr.photos.getSizes&format=json&nojsoncallback=1")
    fun getSizes(
        @Query("api_key") apiKey: String,
        @Query("photo_id") photoId: String
    ): Observable<PhotoSizesResponse>
}