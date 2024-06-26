package com.example.meetapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.meetapp.data.UserRepository
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
    val uid: String,
    val message: String,
    val timestamp: String,
    val recived: Boolean
)

data class ChatConfig(
    val uid: String,
    val token: String,
    val apiId: String,
    val channelName: String
)

class ChatViewModel : ViewModel() {

    private lateinit var rtmClient: RtmClient
    private val _chatMessages = mutableStateListOf<ChatMessage>()
    val chatMessages: List<ChatMessage> get() = _chatMessages

    private val _chatServiceFailed = mutableStateOf<Boolean>(false)
    val chatServiceFialed: Boolean get() = _chatServiceFailed.value
    var chatConfig: ChatConfig = ChatConfig("", "", "", "")

    init {
        Log.d(TAG, "Log all the s: ")
    }

    fun sendMessage(message: String) {
        val timestamp =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
        val chatMessage = ChatMessage(chatConfig.uid, message, timestamp, false)
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

    fun setChatFailed(failed: Boolean) {
        _chatServiceFailed.value = failed
    }

    fun addMessage(chatMessage: ChatMessage) {
        _chatMessages.add(chatMessage)
    }

    fun initChatServices(
        chatConfig: ChatConfig
    ) {
        this.chatConfig = chatConfig
        try {
            val rtmConfig = RtmConfig.Builder(chatConfig.apiId, chatConfig.uid)
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

    fun destroy() {
        rtmClient.unsubscribe(chatConfig.channelName, object : ResultCallback<Void> {
            override fun onSuccess(responseInfo: Void?) {}
            override fun onFailure(errorInfo: ErrorInfo?) {}
        })
        rtmClient.logout(object : ResultCallback<Void> {
            override fun onSuccess(responseInfo: Void?) {}
            override fun onFailure(errorInfo: ErrorInfo?) {}
        })
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
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
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
            if(event?.publisherId !=null){
                UserRepository.registerUid(event.publisherId)
            }
            Log.d(TAG, "onPressence: ${event?.publisherId}")
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

