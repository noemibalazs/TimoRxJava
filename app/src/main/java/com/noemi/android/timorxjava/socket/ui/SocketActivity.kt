package com.noemi.android.timorxjava.socket.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.noemi.android.timorxjava.R
import com.noemi.android.timorxjava.socket.adapter.ChatAdapter
import com.noemi.android.timorxjava.socket.data.ChatMessage
import com.noemi.android.timorxjava.utils.Constants.CHAT_POINT_URL
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposables
import kotlinx.android.synthetic.main.activity_socket.*
import java.net.URISyntaxException

@AndroidEntryPoint
class SocketActivity : AppCompatActivity() {

    private var socket: Socket? = null

    private val viewModel: ChatViewModel by viewModels()

    private val adapter: ChatAdapter by lazy {
        ChatAdapter()
    }
    private val inputManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)

        socket = initSocket()
        connectSocket()

        chatRecycleView.adapter = adapter

        viewModel.initChatMessage()
        initObservers()

        etMessage.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p: TextView, id: Int, event: KeyEvent?): Boolean {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    val text = etMessage.text.toString().trimEnd()
                    return when (text.isNotBlank()) {
                        true -> {
                            val message = Gson().toJson(ChatMessage(message = text))
                            socket?.emit("chat message", message)
                            val emitter = Emitter.Listener {
                                viewModel.chatMessage.onNext(it[0] as String)
                            }
                            socket?.on("chat message", emitter)
                            Disposables.fromAction {
                                socket?.off("chat message", emitter)
                            }
                            etMessage.setText("")
                            closeKeyboard()
                            true
                        }
                        false -> {
                            etMessage.setText("")
                            closeKeyboardEmptyInput()
                            Toast.makeText(this@SocketActivity, "Message should be not empty", Toast.LENGTH_LONG).show()
                            false
                        }
                    }
                }
                return false
            }
        })
    }

    private fun initSocket(): Socket? {
        return try {
            IO.socket(CHAT_POINT_URL)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            null
        }
    }

    private fun connectSocket() {
        socket?.on("connect") {
            Log.d(TAG, "Listening the socket")
        }
        socket?.connect()
    }

    @SuppressLint("CheckResult")
    private fun initObservers() {
        viewModel.getChatMessage().subscribe {
            adapter.submitList(null)
            adapter.submitList(it)
        }
    }

    private fun closeKeyboardEmptyInput() {
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun closeKeyboard() {
        inputManager.hideSoftInputFromWindow(inputLayout.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
    }

    override fun onDestroy() {
        socket?.disconnect()
        super.onDestroy()
    }

    companion object {
        const val TAG = "SocketActivity"
    }
}