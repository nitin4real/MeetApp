package com.example.meetapp.controller

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.meetapp.data.UserRepository
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class VideoConfig(
    val userId: String,
    val token: String,
    val apiId: String,
    val channelName: String
)

enum class VIEW_MODE {
    grid,
    spotlight
}

data class VideoView(val uid: Int, val isVideoActive: Boolean)
class VideoViewModel : ViewModel() {

    var videoEngine: RtcEngine? = null
    lateinit var videoConfig: VideoConfig
    private val _activeVideoView = mutableStateListOf<VideoView>()

    lateinit var context: Context
    val activeViewView: List<VideoView> get() = _activeVideoView
    private val _activeSpeakerUid = mutableStateOf<String>("")
    val activeSpeakerUid: String get() = _activeSpeakerUid.value

    private val _isVideoOn = mutableStateOf(true)
    val isVideoOn get() = _isVideoOn.value
    private val _showSpotlight = mutableStateOf(true)
    val showSpotlight get() = _showSpotlight.value


    private val _isMicOn = mutableStateOf(true)
    val isMicOn get() = _isMicOn.value

    private val _ViewMode = mutableStateOf(VIEW_MODE.grid)
    val viewMode get() = _ViewMode.value


    fun addVideoView(uid: Int, isActive: Boolean) {
        _activeVideoView.add(VideoView(uid, isActive))
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
            addVideoView(videoConfig.userId.toInt(), true)
            videoEngine?.enableAudioVolumeIndication(700, 3, true)
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

    fun toggleViewMode() {
        if (viewMode == VIEW_MODE.grid) {
            _ViewMode.value = VIEW_MODE.spotlight
        } else {
            _ViewMode.value = VIEW_MODE.grid
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
            val updatedView = VideoView(uid, isActive)
            if (index != -1) {
                _activeVideoView[index] = updatedView
            }
        }
    }

    val sessionEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            UserRepository.registerUid(uid.toString())
            Log.d(TAG, "onUserJoined: Remote User Joined with uid -> $uid")
            addVideoView(uid, true)
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
                            if(_activeSpeakerUid.value != videoConfig.userId){
                                resetSpotlight()
                                _activeSpeakerUid.value = videoConfig.userId
                            }
                        } else {
                            if(_activeSpeakerUid.value != uid.toString()){
                                resetSpotlight()
                                _activeSpeakerUid.value = uid.toString()
                            }
                        }
                    }
                }
            }
        }

        fun resetSpotlight(){
            _showSpotlight.value = false
            GlobalScope.launch {
                delay(500)
                _showSpotlight.value = true
            }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            super.onUserOffline(uid, reason)
            removeVideoView(uid)
        }
    }

}