package com.noemi.android.timorxjava.socket.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.socket.data.ChatMessage

class ChatVH(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(message: String){
        view.findViewById<AppCompatTextView>(R.id.tvChatMessage).text = message
    }
}