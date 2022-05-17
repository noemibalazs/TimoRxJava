package com.noemi.android.timorxjava.socket.adapter

import androidx.recyclerview.widget.DiffUtil

class ChatDifUtil : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}