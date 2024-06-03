package com.example.meetapp.controller

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.meetapp.data.UserRepository
import com.example.meetapp.model.RetrofitClient
import com.example.meetapp.services.ApiService
import com.example.meetapp.services.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Configs(val videoConfig: VideoConfig,val chatConfig: ChatConfig)
class LoginViewModel : ViewModel() {

    private var _isTokenSuccess = mutableStateOf<Boolean>(true)
    val isTokenSuccess: Boolean get() = _isTokenSuccess.value

    lateinit var configs: Configs

    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }
    fun setTokenSuccess (status:Boolean){
        _isTokenSuccess.value = status
    }
    fun getAgoraToken(
        userId: String,
        channelName: String,
        navigateToConfigs: (Configs)->Unit
    ) {
        _isTokenSuccess.value = true
        apiService.getUsers(userId, channelName).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    tokenResponse?.let {
                        val videoConfig =
                            VideoConfig(it.uid, it.tokens.rtcToken, it.appId, channelName)
                        val chatConfig =
                            ChatConfig(it.uid, it.tokens.rtmToken, it.appId, channelName)
                        configs = Configs(videoConfig,chatConfig)
                        UserRepository.registerUid(it.uid)
                        navigateToConfigs(configs)
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                _isTokenSuccess.value = false
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }
}