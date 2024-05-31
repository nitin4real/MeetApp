package com.example.meetapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.ViewModel
import io.agora.rtm.ErrorInfo
import io.agora.rtm.MessageEvent
import io.agora.rtm.PresenceEvent
import io.agora.rtm.PublishOptions
import io.agora.rtm.ResultCallback
import io.agora.rtm.RtmClient
import io.agora.rtm.RtmConfig
import io.agora.rtm.RtmEventListener
import io.agora.rtm.SubscribeOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val TAG = "Test Agora Android"

data class ChatMessage(
    val userName: String,
    val message: String,
    val timestamp: String,
    val recived: Boolean
)

data class ChatConfig(
    val userId: String,
    val token: String,
    val apiId: String,
    val channelName: String
)

class ChatViewModel : ViewModel() {

    lateinit var rtmClient: RtmClient
    private val _chatMessages = mutableStateListOf<ChatMessage>()
    val chatMessages: List<ChatMessage> get() = _chatMessages

    private val _chatServiceFailed = mutableStateOf<Boolean>(false)
    val chatServiceFialed: Boolean get() = _chatServiceFailed.value
    var chatConfig: ChatConfig = ChatConfig("", "", "", "")

    init {
        Log.d(TAG, "Log all the s: ")
    }

    fun sendMessage(chatMessage: ChatMessage) {
        rtmClient.publish(
            chatConfig.channelName,
            chatMessage.message,
            object : PublishOptions() {},
            object : ResultCallback<Void> {
                override fun onSuccess(responseInfo: Void?) {
                    addMessage(chatMessage)
                }

                override fun onFailure(errorInfo: ErrorInfo?) {
                    Log.d(TAG, "onFailure: Error In sending Message")
                }
            }
        )
    }

    fun setChatFailed (failed: Boolean){
        _chatServiceFailed.value = failed
    }

    fun addMessage(chatMessage: ChatMessage) {
        _chatMessages.add(chatMessage)
    }

    fun initChatServices(
        chatConfig: ChatConfig = ChatConfig(
            "54321",
            "007eJxSYPir+HvxvX2PzrfVmC+b9GytWfm+N2yTjr1371Tpfig8oXOeAoNZokGKQZKhUZqZgblJYmKKhaFFsqVlWpqlsalpiomJSZ1yVFqBOANDwN9VDEwMjGAM4rOASWYGQyNjVgZTE2MjQ5AMRA4qAAgAAP//8AYj1A==",
            "6a0d0b12f6074aad818c99ff9355d444",
            "123",
        )
    ) {
        this.chatConfig = chatConfig
        try {
            val rtmConfig = RtmConfig.Builder(chatConfig.apiId, chatConfig.userId)
                .eventListener(connectionCallbacks)
                .build()

            rtmClient = RtmClient.create(rtmConfig)
            rtmClient.login(
                chatConfig.token,
                loginCallBacks
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val loginCallBacks = object : ResultCallback<Void> {
        override fun onSuccess(responseInfo: Void?) {
            val options = SubscribeOptions(true, true, false, false)
            rtmClient.subscribe(chatConfig.channelName, options, subscriptionCallbacks)
            Log.d(TAG, "onSuccess: loging success")
        }

        override fun onFailure(errorInfo: ErrorInfo?) {
            setChatFailed(true)
            Log.d(TAG, "login failed: loging failure ${errorInfo.toString()}")
        }
    }

    private val connectionCallbacks = object : RtmEventListener {
        override fun onMessageEvent(event: MessageEvent?) {
            Log.d(TAG, "onMessageEvent: ${event?.message}")
            val timestamp =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ?: ""
            event?.let {
                addMessage(
                    ChatMessage(
                        it.publisherId,
                        (it.message?.getData() ?: "").toString(),
                        timestamp,
                        true
                    )
                )
            }
        }

        override fun onPresenceEvent(event: PresenceEvent?) {
            Log.d(TAG, "onPressence: ${event}")
        }
    }

    private val subscriptionCallbacks = object : ResultCallback<Void> {
        override fun onSuccess(responseInfo: Void?) {
            Log.d(TAG, "onSuccess: subscription success${responseInfo.toString()}")
        }

        override fun onFailure(errorInfo: ErrorInfo?) {
            setChatFailed(true)
            Log.d(TAG, "onFailure: subscription failure ${errorInfo.toString()}")
        }
    }
}

