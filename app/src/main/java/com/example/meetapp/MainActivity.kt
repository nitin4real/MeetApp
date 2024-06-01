package com.example.meetapp

import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.meetapp.controller.VideoView
import com.example.meetapp.controller.VideoViewModel
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


data class VideoConfig(
    val userId: String,
    val token: String,
    val apiId: String,
    val channelName: String
)


const val TAG = "IN MAIN RUNNING TRIP"

class MainActivity : ComponentActivity() {
    private val videoVM: VideoViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        videoVM.startEngine(baseContext);

//        val t = SurfaceView(this)
//        videoVM.videoEngine.setupLocalVideo(VideoCanvas(t,VideoCanvas.RENDER_MODE_FIT, videoConfig.userId.toInt()))
//        videoVM.addVideoView(t)
//        updateTheArray push t2 also

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(modifier = Modifier.padding(50.dp)){
                RenderSurfaceViews(surfaceViews = videoVM.activeViewView)
            }
        }
    }
}

@Composable
fun RenderSurfaceViews(surfaceViews: List<VideoView>) {
    Column(
        modifier = Modifier.fillMaxSize().border(2.dp, Color.Black),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        surfaceViews.forEach { surfaceView ->
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
/*
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
 */