package com.example.meetapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.meetapp.R
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.ui.theme.CustomColors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(chatVM: ChatViewModel) {
    var newMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(chatVM.chatMessages.size) { index ->
                ChatMessageItem(chatMessage = chatVM.chatMessages[index])
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black, // Cursor color
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    containerColor = CustomColors.secondary
                ),
                shape = RoundedCornerShape(20.dp),
                placeholder = { Text(text = "Type a message here")},
                trailingIcon = {
                    IconButton(onClick = {
                        if(newMessage.isNotBlank()){
                            chatVM.sendMessage(newMessage)
                            newMessage = ""
                        }
                    }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.send),
                        contentDescription = "Send",
                        modifier = Modifier.size(30.dp),
                        tint = if(newMessage.isNotBlank()) CustomColors.primary else Color.Black
                    )
                    }
                },
            )
        }
    }
}

