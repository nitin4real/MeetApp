package com.example.meetapp.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meetapp.controller.ChatMessage
import com.example.meetapp.ui.theme.CustomColors

@Composable
fun ChatMessageItem(chatMessage: ChatMessage) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)) // Clip the column to have rounded corners
            .background(color = if (chatMessage.recived) CustomColors.tertiary else CustomColors.secondary)
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = if (chatMessage.recived) Alignment.Start else Alignment.End
    ) {
        if (chatMessage.recived) Text(
            text = "send by: ${chatMessage.userName}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(text = chatMessage.message, style = MaterialTheme.typography.bodyLarge)
        Text(
            text = chatMessage.timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}