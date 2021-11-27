package com.noemi.android.timorxjava.flickr.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.noemi.android.timorxjava.flickr.model.FlickrImage
import com.noemi.android.timorxjava.flickr.remote_data.RemoteAPI
import com.noemi.android.timorxjava.flickr.response.PhotoInfoResponse
import com.noemi.android.timorxjava.flickr.response.PhotoSizesResponse
import com.noemi.android.timorxjava.flickr.response.SearchResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FlickrViewModel @Inject constructor(private val remoteAPI: RemoteAPI) : ViewModel() {

    private val composite = CompositeDisposable()
    val progressObserver = MutableLiveData<Boolean>()
    val errorObserver = MutableLiveData<String>()
    val flickrImages = MutableLiveData<List<FlickrImage>>()

    private fun searchByTag(search: String): Observable<SearchResponse> {
        return remoteAPI.searchPhotos(search)
    }

    private fun getPhotoInfo(id: String): Observable<PhotoInfoResponse> {
        return remoteAPI.getPhotoInfo(id)
    }

    private fun getPhotoSizes(id: String): Observable<PhotoSizesResponse> {
        return remoteAPI.getPhotoSizes(id)
    }

    fun getFlickrImages(tag: String) {
        val disposable = Observable.just(true)
            .doOnNext { _ -> Log.d(TAG, "Search is clicked") }
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                progressObserver.value = true
            }
            .flatMap {
                searchByTag(tag)
            }
            .map { searchResponse -> searchResponse.photos.photo }
            .doOnNext { photos -> Log.d(TAG, "The size of the photos: ${photos.size}") }
            .flatMap { flickrPhotos ->
                if (!flickrPhotos.isNullOrEmpty()) {
                    return@flatMap Observable.fromIterable(flickrPhotos)
                        .doOnNext { photo -> Log.d(TAG, "Photo id: ${photo.id}") }
                        .concatMap { photo ->
                            Observable.combineLatest(
                                getPhotoInfo(photo.id)
                                    .map { infos -> infos.photoInfo },
                                getPhotoSizes(photo.id)
                                    .map { res -> res.seizes },
                                { info, seizes -> FlickrImage.createImage(info, seizes) }
                            )
                        }
                        .doOnNext { photo -> Log.d(TAG, "Photo id: ${photo.id}") }
                        .toList()
                        .toObservable()
                } else {
                    return@flatMap Observable.just(emptyList())
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                progressObserver.value = false
            }
            .subscribe(
                { images -> flickrImages.value = images },
                { error ->
                    errorObserver.value = "Error getting images from the Flickr API."
                    Log.d(TAG, "${error.printStackTrace()}")
                }
            )

        composite.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        composite.clear()
    }

    companion object {
        private val TAG = FlickrViewModel::class.java.simpleName
    }

}