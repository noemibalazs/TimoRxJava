package com.noemi.android.timorxjava.file

import androidx.recyclerview.widget.DiffUtil
import java.io.File

class FileDifUtil : DiffUtil.ItemCallback<File>() {

    override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.name == newItem.name
    }
}