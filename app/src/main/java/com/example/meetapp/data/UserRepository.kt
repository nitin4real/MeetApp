package com.example.meetapp.data

import android.util.Log
import com.example.meetapp.model.RetrofitClient
import com.example.meetapp.services.ApiService
import com.example.meetapp.services.UserNameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserRepository {
    private val uidNameMap: MutableMap<String, String> = mutableMapOf()
    private var channelName: String = ""

    private val apiService: ApiService by lazy {
        RetrofitClient.instance.create(ApiService::class.java)
    }

    fun setChannelName(channelName:String){
        this.channelName = channelName
    }
    fun getChannelName():String{
        return channelName
    }
    fun registerUid(uid: String) {
        apiService.getUserName(uid).enqueue(object : Callback<UserNameResponse> {
            override fun onResponse(
                call: Call<UserNameResponse>,
                response: Response<UserNameResponse>
            ) {
                if (response.isSuccessful) {
                    val userNameResponse = response.body()
                    userNameResponse?.let {
                        if(it.userName == null){
                            addValue(uid, "user")
                        } else {
                            addValue(uid,it.userName)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<UserNameResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

    fun addValue(uid: String, name: String) {
        uidNameMap[uid] = name
    }

    fun getName(uid: String): String? {
        val userName = uidNameMap[uid]
        return if (userName == null) "user" else userName
    }
}
