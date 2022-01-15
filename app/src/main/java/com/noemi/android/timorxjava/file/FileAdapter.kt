package com.noemi.android.timorxjava.file

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.noemi.android.timorxjava.R
import java.io.File

typealias FileClickListener = (File) -> Unit

class FileAdapter(private val fileClickListener: FileClickListener?) : ListAdapter<File, FileVH>(FileDifUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileVH {
        return FileVH(LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false), fileClickListener)
    }

    override fun onBindViewHolder(holder: FileVH, position: Int) {
        holder.bind(getItem(position))
    }
}