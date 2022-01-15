package com.noemi.android.timorxjava.file

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.noemi.android.timorxjava.R
import java.io.File

typealias FileClickListener = (File) -> Unit
typealias FileLongClickListener = (File) -> Unit

class FileAdapter(
    private val fileClickListener: FileClickListener?,
    private val fileLongClickListener: FileLongClickListener?
) : ListAdapter<File, FileVH>(FileDifUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileVH {
        return FileVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false),
            fileClickListener,
            fileLongClickListener
        )
    }

    override fun onBindViewHolder(holder: FileVH, position: Int) {
        holder.bind(getItem(position))
    }
}