package com.example.meetapp.view

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.meetapp.R
import com.example.meetapp.controller.MeetViewModel
import com.example.meetapp.controller.VideoViewModel
import com.example.meetapp.ui.theme.CustomColors

@Composable
fun BottomButtons(meetVM: MeetViewModel, videoVM: VideoViewModel) {
    val context = LocalContext.current
    val activity = context as? Activity
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            RoundButton(
                R.drawable.mute,
                if (videoVM.isMicOn) CustomColors.tertiary else Color.Red
            ) {
                val toastText = if (videoVM.isMicOn) "Muted" else "Unmuted"
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                videoVM.toggleAudioMode()
            }
            RoundButton(
                R.drawable.videooff,
                if (videoVM.isVideoOn) CustomColors.tertiary else Color.Red
            ) {
                val toastText = if (videoVM.isVideoOn) "Video Share off" else "Video Share on"
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                videoVM.toggleVideoMode()
            }
            RoundButton(
                R.drawable.switch_video_view,
                Color.Green
            ) {
//                videoVM.videoEngine.muteLocalVideoStream(false)
            }
            RoundButton(
                R.drawable.leave,
                Color.Red
            ) {
                activity?.finish()
            }
        }
    }
}