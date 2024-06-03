package com.example.meetapp.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.meetapp.R
import com.example.meetapp.controller.ChatViewModel
import com.example.meetapp.controller.MeetViewModel
import com.example.meetapp.controller.VIEW_TYPE
import com.example.meetapp.controller.VideoViewModel
import com.example.meetapp.data.UserRepository
import com.example.meetapp.ui.theme.CustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetComponent(videoVM: VideoViewModel, chatVM: ChatViewModel, meetVM: MeetViewModel) {
    val channelName = UserRepository.getChannelName()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("In Meet: ${channelName}", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = CustomColors.primary
                ),
                actions = {
                    IconButton(onClick = { meetVM.switchView() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(
                                if (meetVM.selectedView === VIEW_TYPE.video) R.drawable.chat
                                else androidx.core.R.drawable.ic_call_answer_video
                            ),
                            contentDescription = "Disconnect",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
            )
        },
        content = { it ->
            MeetContent(
                modifier = Modifier.padding(it),
                videoVM,
                chatVM,
                meetVM
            )
        }
    )
}