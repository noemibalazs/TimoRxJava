package com.noemi.android.timorxjava.file

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.noemi.android.timorxjava.R
import java.io.File

class FileVH(
    view: View,
    private val callback: FileClickListener?,
    private val fileLongClickListener: FileLongClickListener?
) : RecyclerView.ViewHolder(view) {

    fun bind(file: File) {
        val fileTag = itemView.findViewById<AppCompatTextView>(R.id.tvFileTag)
        fileTag.text = if (file.isDirectory) file.name + "/" else file.name
        itemView.setOnClickListener {
            callback?.let { callback.invoke(file) }
        }

        if (file.name.endsWith(".png") || file.name.endsWith(".jpg") || file.name.endsWith(".jpeg")) {
            itemView.setOnLongClickListener {
                fileLongClickListener?.invoke(file)
                true
            }
        }
    }
}