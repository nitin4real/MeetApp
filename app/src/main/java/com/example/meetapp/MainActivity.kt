package com.example.meetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.view.ChatScreen

class MainActivity : ComponentActivity() {
    private val chatVM: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        chatVM.initChatServices()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatScreen(chatVM = chatVM)
        }
    }
}
