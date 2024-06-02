package com.example.meetapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.controller.MeetViewModel
import com.example.meetapp.controller.VIEW_TYPE
import com.example.meetapp.controller.VideoViewModel

@Composable
fun MeetContent(
    modifier: Modifier = Modifier,
    videoVM: VideoViewModel,
    chatVM: ChatViewModel,
    meetVM: MeetViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (meetVM.selectedView === VIEW_TYPE.video) {
            VideoScreen(videoVM)
            BottomButtons(meetVM, videoVM)
        } else {
            ChatScreen(chatVM)
        }
    }
}