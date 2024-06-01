package com.example.meetapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.controller.Configs
import com.example.meetapp.controller.VideoViewModel
import com.google.gson.Gson


class MeetActivity : ComponentActivity() {
    private val videoVM: VideoViewModel by viewModels()
    private val chatVM: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        val configsJson = intent.getStringExtra("configs")
        val configs: Configs = gson.fromJson(configsJson, Configs::class.java)

        videoVM.startEngine(baseContext, configs.videoConfig);
        chatVM.initChatServices(configs.chatConfig);

        enableEdgeToEdge()
        setContent {
            ChatScreen(chatVM)
            Box(modifier = Modifier.padding(50.dp)){
                VideoScreen(videoVM)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        videoVM.destroy()
        chatVM.destroy()
    }
}
