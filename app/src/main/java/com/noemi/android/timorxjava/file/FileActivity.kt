package com.noemi.android.timorxjava.file

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.showDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_file.*
import java.io.File

@AndroidEntryPoint
class FileActivity : AppCompatActivity() {

    private val viewModel: FileViewModel by viewModels()

    private val adapter by lazy { FileAdapter(fileListener, fileLongClickListener) }

    private val fileListener: FileClickListener = {
        createFileClickObservable(it)
    }

    private val fileLongClickListener: FileLongClickListener = {
        displayImage(it)
    }

    private val backObservable = PublishSubject.create<Any>()
    private val homeObservable = PublishSubject.create<Any>()
    private val fileClickObservable = PublishSubject.create<File>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        initRV()
        initClickListeners()
        initFileHandler()
    }

    private fun initRV() {
        rvFile.adapter = adapter
    }

    private fun initFileHandler() {
        val rootFile = File(Environment.getExternalStorageDirectory().path)
        val selectedDir = BehaviorSubject.createDefault(rootFile)
        Log.d("TAG", "Path: $rootFile");

        val fileBackObservable: Observable<File> = backObservable.map {
            selectedDir.value?.parentFile
        }
        val fileHomeObservable: Observable<File> = homeObservable.map { rootFile }

        val selectedDirSubscription =
            Observable.merge(fileClickObservable, fileBackObservable, fileHomeObservable)
                .subscribe { it?.let { selectedDir.onNext(it) } }


        val showFileDetails = selectedDir.subscribeOn(Schedulers.io())
            .doOnNext { file -> Log.d("TAG", "File name: ${file.name}") }
            .switchMap { file ->
                createFileObservable(file)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                adapter.submitList(null)
                adapter.submitList(it)
                Log.d("TAG", "File size: ${it.size}")
            }

        viewModel.disposable.add(selectedDirSubscription)
        viewModel.disposable.add(showFileDetails)
    }

    private fun initClickListeners() {
        tvBack.setOnClickListener {
            backObservable.onNext(Any())
        }

        tvHome.setOnClickListener {
            homeObservable.onNext(Any())
        }
    }

    private fun createFileClickObservable(file: File) {
        if (file.isDirectory)
            fileClickObservable.onNext(file)
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

    private fun displayImage(file: File) {
        showDialogFragment(FilePictureFragment().apply {
            isCancelable = true
            arguments = Bundle().apply {
                putString(EXTRA_FILE_PATH, file.absolutePath)
            }
        }, DIALOG_TAG)
    }

    companion object {
        const val DIALOG_TAG = "dialog_tag"
        const val EXTRA_FILE_PATH = "extra_file_path"
    }
}