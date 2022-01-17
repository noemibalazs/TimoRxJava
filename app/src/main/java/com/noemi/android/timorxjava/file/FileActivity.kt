package com.noemi.android.timorxjava.file

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.showDialogFragment
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        initRV()
        initClickListeners()
        viewModel.initFileHandler()
        initObserver()
    }

    private fun initRV() {
        rvFile.adapter = adapter
    }

    private fun initClickListeners() {
        tvBack.setOnClickListener {
            viewModel.backObservable.onNext(Any())
        }

        tvHome.setOnClickListener {
            viewModel.homeObservable.onNext(Any())
        }
    }

    private fun initObserver() {
        viewModel.files.observe(this, Observer {
            adapter.submitList(null)
            adapter.submitList(it)
        })
    }

    private fun createFileClickObservable(file: File) {
        if (file.isDirectory)
            viewModel.fileClickObservable.onNext(file)
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