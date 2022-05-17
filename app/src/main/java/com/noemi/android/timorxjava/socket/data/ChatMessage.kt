package com.noemi.android.timorxjava.socket.data

import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val timeStamp: Long = Date().time
){
    override fun toString(): String {
        return "ChatMessage: id - $id - message: $message - timeStamp: $timeStamp"
    }
}