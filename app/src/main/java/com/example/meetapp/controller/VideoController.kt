package com.example.meetapp.controller

import android.content.Context
import android.util.Log
import android.view.SurfaceView
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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

data class VideoView(val video: SurfaceView, val uid: Int, val isVideoActive: Boolean)
class VideoViewModel : ViewModel() {

    var videoEngine: RtcEngine? = null
    private lateinit var videoConfig: VideoConfig
    private val _activeVideoView = mutableStateListOf<VideoView>()

    lateinit var context: Context
    val activeViewView: List<VideoView> get() = _activeVideoView
    private val _activeSpeakerUid = mutableStateOf<String>("")
    val activeSpeakerUid: String get() = _activeSpeakerUid.value

    private val _isVideoOn = mutableStateOf(true)
    val isVideoOn get() = _isVideoOn.value

    private val _isMicOn = mutableStateOf(true)
    val isMicOn get() = _isMicOn.value


    fun addVideoView(videoView: SurfaceView, uid: Int, isActive: Boolean) {
        _activeVideoView.add(VideoView(videoView, uid, isActive))
    }

    fun startEngine(context: Context, videoConfig: VideoConfig): Unit {
        this.videoConfig = videoConfig
        this.context = context
        val config = RtcEngineConfig()
        config.mContext = context
        config.mAppId = videoConfig.apiId
        config.mEventHandler = sessionEventHandler
        try {
            videoEngine = RtcEngine.create(config)
            joinMeet()
            videoEngine?.enableVideo();
            _isVideoOn.value = true
            videoEngine?.startPreview()
            val localView = SurfaceView(context)
            videoEngine?.setupLocalVideo(
                VideoCanvas(
                    localView,
                    VideoCanvas.RENDER_MODE_FIT,
                    videoConfig.userId.toInt()
                )
            )
            addVideoView(localView, videoConfig.userId.toInt(), true)
            videoEngine?.enableAudioVolumeIndication(300, 3, true)
            Log.d(TAG, "Start Engine Success. Brrrr brrrr brrr(Noise of the engine)")
        } catch (e: Exception) {
            Log.d(TAG, "startEngin: Error occurect while createing view engine${e.message}")
        }
    }

    fun joinMeet() {
        val options = ChannelMediaOptions()
        options.clientRoleType = 1
        options.channelProfile = 1
        options.publishCameraTrack = true
        videoEngine?.joinChannel(
            videoConfig.token,
            videoConfig.channelName,
            videoConfig.userId.toInt(),
            options
        )
    }

    fun removeVideoView(uid: Int) {
        _activeVideoView.remove(
            _activeVideoView.find {
                it.uid == uid
            })
    }

    fun toggleVideoMode() {
        if (_isVideoOn.value) {
            videoEngine?.muteLocalVideoStream(true)
            _isVideoOn.value = false
        } else {
            videoEngine?.muteLocalVideoStream(false)
            _isVideoOn.value = true
        }
        updateVideoViews(videoConfig.userId.toInt(), _isVideoOn.value)
    }

    fun toggleAudioMode() {
        if (_isMicOn.value) {
            videoEngine?.muteLocalAudioStream(true)
            _isMicOn.value = false
        } else {
            videoEngine?.muteLocalAudioStream(false)
            _isMicOn.value = true
        }
    }

    fun destroy() {
        videoEngine?.disableVideo()
        videoEngine?.disableAudio()
        videoEngine?.leaveChannel()
        videoEngine = null
        RtcEngine.destroy()
    }

    fun updateVideoViews(uid: Int, isActive: Boolean) {
        val index = _activeVideoView.indexOfFirst { it.uid == uid }
        if (index != -1) {
            val currentView = _activeVideoView[index]
            val updatedView = VideoView(currentView.video, uid, isActive)
            if (index != -1) {
                _activeVideoView[index] = updatedView
            }
        }
    }

    val sessionEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined: Remote User Joined with uid -> $uid")
            val remoteView = SurfaceView(context)
            videoEngine?.setupRemoteVideo(VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_FIT, uid))
            addVideoView(remoteView, uid, true)
        }

        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            super.onUserMuteVideo(uid, muted)
            updateVideoViews(uid, !muted)
        }

        override fun onAudioVolumeIndication(
            speakers: Array<out AudioVolumeInfo>?,
            totalVolume: Int
        ) {
            super.onAudioVolumeIndication(speakers, totalVolume)
            if (speakers != null) {
                for (speaker in speakers) {
                    val uid = speaker.uid
                    val volume = speaker.volume
                    if (volume > 100) {
                        if (uid === 0) {
                            _activeSpeakerUid.value = videoConfig.userId
                        } else {
                            _activeSpeakerUid.value = uid.toString()
                        }
                    }
                }
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            removeVideoView(uid)
        }
    }

}