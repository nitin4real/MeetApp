package com.example.meetapp.view

import android.content.Context
import android.view.SurfaceView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.meetapp.R
import com.example.meetapp.controller.VIEW_MODE
import com.example.meetapp.controller.VideoViewModel
import com.example.meetapp.data.UserRepository
import io.agora.rtc2.video.VideoCanvas

@Composable
fun VideoScreen(videoVM: VideoViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        val context = LocalContext.current
        val activeViews = videoVM.activeViewView
        val isSpotlightMode = videoVM.viewMode === VIEW_MODE.spotlight
        val spotlightItem = activeViews.find {
            it.uid.toString() == videoVM.activeSpeakerUid
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isSpotlightMode) 2 else 1),
            modifier = Modifier
                .weight(if (isSpotlightMode) 0.5f else 1f)
        ) {
            items(activeViews.size) { index ->
                val currentView = activeViews[index]
                val isSpeaking = videoVM.activeSpeakerUid == currentView.uid.toString()
                val borderColor = if (isSpeaking) Color.Yellow else Color.White
                val isActive = currentView.isVideoActive
                val uid = currentView.uid
                val userName = UserRepository.getName(uid.toString())
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${userName}",
                        fontSize = 16.sp,
                    )
                    if (isActive && !(isSpeaking &&  isSpotlightMode)) {
                        AndroidView(
                            factory = { videoViewFactory(currentView.uid, videoVM, context) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                                .clip(RoundedCornerShape(10.dp))
                                .border(3.dp, borderColor,shape = RoundedCornerShape(10.dp)),
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.user5),
                            contentDescription = "Dummy User",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16 / 9f)
                                .border(3.dp, borderColor, shape = RoundedCornerShape(10.dp)),
                        )
                    }
                }
            }
        }

        if (isSpotlightMode && videoVM.showSpotlight) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val uid = videoVM.activeSpeakerUid
                    val userName = UserRepository.getName(uid)
                    if (spotlightItem != null) {
                        if (spotlightItem.isVideoActive) {
                            Text(
                                text = "${userName}",
                                fontSize = 18.sp,
                            )
                            AndroidView(
                                factory = { videoViewFactory(videoVM.activeSpeakerUid.toInt(), videoVM, context) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .border(3.dp, color = Color.Yellow, shape = RoundedCornerShape(10.dp)),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.user5),
                                contentDescription = "Dummy User${uid}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16 / 9f)
                                    .border(3.dp, Color.Yellow),
                            )
                        }

                    }
                }
            }
        }
    }
}


fun videoViewFactory(item: Int, videoVM: VideoViewModel, context: Context): SurfaceView {
    val view = SurfaceView(context)
    if (item != null && videoVM.videoConfig != null) {
        val isSelf = videoVM.videoConfig.userId == item.toString()
        if (isSelf) {
            videoVM.videoEngine?.setupLocalVideo(
                VideoCanvas(
                    view,
                    VideoCanvas.RENDER_MODE_FIT,
                    item
                )
            )
        } else {
            videoVM.videoEngine?.setupRemoteVideo(
                VideoCanvas(
                    view,
                    VideoCanvas.RENDER_MODE_FIT,
                    item
                )
            )
        }
    }
    return view
}