package com.noemi.android.timorxjava.file

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.noemi.android.timorxjava.R
import kotlinx.android.synthetic.main.fragment_file_picture.*
import java.io.File

class FilePictureFragment : DialogFragment() {

    private val marginVertical by lazy {
        resources.getDimensionPixelSize(R.dimen.offset_huge)
    }

    private val marginHorizontal by lazy {
        resources.getDimensionPixelSize(R.dimen.offset_large)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_file_picture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val path = arguments?.getString(FileActivity.EXTRA_FILE_PATH)
        path?.let {
            Glide.with(requireContext()).load(File(it)).error(R.drawable.tiger).into(ivFile)
        }
    }

    override fun onStart() {
        super.onStart()
        val displayRect = Rect()
        dialog?.window?.decorView?.getWindowVisibleDisplayFrame(displayRect)
        val screenWidth = displayRect.width()
        val screenHeight = displayRect.height()
        dialog?.window?.setLayout(
            screenWidth - marginHorizontal * 2,
            screenHeight - marginVertical * 2
        )
    }
}