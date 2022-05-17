package com.noemi.android.timorxjava.socket.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.noemi.android.timorxjava.socket.data.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val chatMessageList = PublishSubject.create<MutableList<String>>()
    var chatMessage = PublishSubject.create<String>()

    fun getChatMessage(): Observable<MutableList<String>> {
        return chatMessageList.observeOn(AndroidSchedulers.mainThread())
    }

    @SuppressLint("CheckResult")
    fun initChatMessage() {
        chatMessage.observeOn(AndroidSchedulers.mainThread())
            ?.map { json ->
                Gson().fromJson(json, ChatMessage::class.java)
            }
            ?.scan(ArrayList(), { previousList: MutableList<ChatMessage>, message: ChatMessage ->
                addMessage(previousList, message)
            })
            ?.flatMap { list ->
                if (!list.isNullOrEmpty()) {
                    return@flatMap Observable.fromIterable(list)
                        .map { it.message }
                        .toList()
                        .toObservable()
                } else {
                    return@flatMap Observable.just(emptyList())
                }
            }
            ?.subscribe(
                { it -> chatMessageList.onNext(it.toMutableSet().toMutableList()) },
                { error -> Log.d(TAG, "Error: ${error.printStackTrace()}") }
            )
    }

    private fun addMessage(previousList: MutableList<ChatMessage>, message: ChatMessage): MutableList<ChatMessage> {
        previousList.add(message)
        return previousList
    }

    companion object {

        const val TAG = "ChatViewModel"
    }
}