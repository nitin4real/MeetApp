package com.example.meetapp.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meetapp.controller.ChatMessage
import com.example.meetapp.controller.ChatViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatMessageItem(chatMessage: ChatMessage) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = chatMessage.userName, style = MaterialTheme.typography.bodyMedium)
        Text(text = chatMessage.message, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = chatMessage.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun ChatScreen(chatVM: ChatViewModel) {
    var newMessage by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("User") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chatVM.chatMessages.size) { index ->
                ChatMessageItem(chatMessage = chatVM.chatMessages[index])
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            BasicTextField(
                value = userName,
                onValueChange = { userName = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            )
            BasicTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    chatVM.sendMessage(ChatMessage(userName, newMessage, timestamp,false))
                    newMessage = ""
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Send")
            }
        }
    }
}

