package com.example.meetapp.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.meetapp.controller.VideoViewModel

@Composable
fun VideoScreen(videoVM: VideoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .border(2.dp, Color.Black),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        videoVM.activeViewView.forEach { surfaceView ->
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .border(3.dp, Color.Red),  // Adjust the aspect ratio as needed
                factory = { surfaceView.video }
            )
        }
    }
}