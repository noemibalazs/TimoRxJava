package com.noemi.android.timorxjava.file

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor() : ViewModel() {

    private val disposable = CompositeDisposable()

    val backObservable = PublishSubject.create<Any>()
    val homeObservable = PublishSubject.create<Any>()
    val fileClickObservable = PublishSubject.create<File>()
    val files = MutableLiveData<MutableList<File>>()
    private var rootFile = File(Environment.getExternalStorageDirectory().path)
    private var selectedDir = BehaviorSubject.createDefault(rootFile)

    fun initFileHandler() {
        Log.d(TAG, "File path: $rootFile");

        val fileBackObservable: Observable<File> = backObservable.map {
            selectedDir.value?.parentFile
        }
        val fileHomeObservable: Observable<File> = homeObservable.map { rootFile }

        val selectedDirSubscription =
            Observable.merge(fileClickObservable, fileBackObservable, fileHomeObservable)
                .subscribe { it?.let { selectedDir.onNext(it) } }

        disposable.add(selectedDirSubscription)
        showDetails()
    }

    private fun showDetails() {
        val showFileDetails = selectedDir.subscribeOn(Schedulers.io())
            .doOnNext { file -> Log.d(TAG, "File name: ${file.name}") }
            .switchMap { file ->
                createFileObservable(file)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                files.value = it
                Log.d(TAG, "File size: ${it.size}")
            }

        disposable.add(showFileDetails)
    }

    private fun createFileObservable(file: File?): Observable<MutableList<File>> {
        return Observable.create { emitter ->
            run {
                try {
                    val files = getFiles(file)
                    emitter.onNext(files)
                    emitter.onComplete()
                } catch (e: Exception) {
                    emitter.onError(e)
                }
            }
        }
    }

    private fun getFiles(file: File?): MutableList<File> {
        val files = mutableListOf<File>()
        file?.listFiles()?.let {
            it.iterator().forEach { file ->
                if (!file.isHidden && file.canRead()) {
                    files.add(file)
                }
            }
        }
        return files
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    companion object {
        const val TAG = "FileViewModel"
    }
}