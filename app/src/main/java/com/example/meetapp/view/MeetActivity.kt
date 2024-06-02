package com.example.meetapp.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.controller.Configs
import com.example.meetapp.controller.MeetViewModel
import com.example.meetapp.controller.VideoViewModel
import com.google.gson.Gson


class MeetActivity : ComponentActivity() {
    private val videoVM: VideoViewModel by viewModels()
    private val chatVM: ChatViewModel by viewModels()
    private val meetVM: MeetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gson = Gson()
        val configsJson = intent.getStringExtra("configs")
        val configs: Configs = gson.fromJson(configsJson, Configs::class.java)

        videoVM.startEngine(baseContext, configs.videoConfig);
        chatVM.initChatServices(configs.chatConfig);

        enableEdgeToEdge()
        setContent {
            MeetComponent(videoVM, chatVM, meetVM)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoVM.destroy()
        chatVM.destroy()
    }
}

