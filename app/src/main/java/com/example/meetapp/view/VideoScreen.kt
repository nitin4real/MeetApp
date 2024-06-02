package com.example.meetapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.meetapp.R
import com.example.meetapp.controller.VideoViewModel

@Composable
fun VideoScreen(videoVM: VideoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(2.dp, Color.Black),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val activeViews = videoVM.activeViewView
        LazyColumn(
            modifier = Modifier
                .weight(1f),
        ) {
            items(activeViews.size) { index ->
                val currentView = activeViews[index]
                val isSpeaking = videoVM.activeSpeakerUid == currentView.uid.toString()
                val borderColor = if (isSpeaking) Color.Yellow else Color.White
                val isActive = currentView.isVideoActive
                Box (
                    modifier = Modifier
                        .fillMaxSize()
                ){
                if (isActive) {
                    AndroidView(
                        factory = { currentView.video },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .border(3.dp, borderColor),
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.user5),
                        contentDescription = "Dummy User",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .border(3.dp, borderColor),
                    )
                }
                Text(currentView.uid.toString(), color = Color.Blue)
                }

            }
        }

    }
}