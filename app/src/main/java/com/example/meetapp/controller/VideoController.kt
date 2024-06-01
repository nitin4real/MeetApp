package com.example.meetapp.controller

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
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

data class VideoView(val video:SurfaceView,val uid:Int)
class VideoViewModel : ViewModel() {

    private lateinit var videoEngine: RtcEngine
    private lateinit var videoConfig: VideoConfig
    private val _activeVideoView = mutableStateListOf<VideoView>()

    lateinit var context: Context
    val activeViewView: List<VideoView> get() = _activeVideoView

    fun addVideoView(videoView: SurfaceView,uid:Int) {
        _activeVideoView.add(VideoView(videoView,uid))
    }

    fun startEngine(context: Context, videoConfig: VideoConfig): Unit {
        this.videoConfig = videoConfig
        this.context = context
        val config = RtcEngineConfig()
        config.mContext = context
        config.mAppId = videoConfig.apiId
        config.mEventHandler = sessionEventHandler
        try{
            videoEngine = RtcEngine.create(config)
            videoEngine.enableVideo();
            videoEngine.startPreview()
            val localView = SurfaceView(context)
            joinMeet()
            videoEngine.setupLocalVideo(VideoCanvas(localView,VideoCanvas.RENDER_MODE_FIT, videoConfig.userId.toInt()))
            addVideoView(localView, videoConfig.userId.toInt())

            Log.d(TAG, "Start Engine Success. Brrrr brrrr brrr(Noise of the engine)")
        } catch (e:Exception){
            Log.d(TAG, "startEngin: Error occurect while createing view engine${e.message}")
        }
    }

    fun joinMeet() {
        val options = ChannelMediaOptions()
        options.clientRoleType = 1
        options.channelProfile = 0
        options.publishCameraTrack = true
        videoEngine.joinChannel(
            videoConfig.token,
            videoConfig.channelName,
            videoConfig.userId.toInt(),
            options
        )
    }
    fun removeVideoView (uid:Int){
        _activeVideoView.remove(
            _activeVideoView.find {
            it.uid == uid
        })
    }

    fun destroy() {
        RtcEngine.destroy()
    }

    val sessionEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined: Remote User Joined with uid -> $uid")
            val remoteView = SurfaceView(context)
            videoEngine.setupRemoteVideo(VideoCanvas(remoteView,VideoCanvas.RENDER_MODE_FIT, uid))
            addVideoView(remoteView, uid)
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            removeVideoView(uid)
        }
    }

}