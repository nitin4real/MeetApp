package com.example.meetapp.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

data class UserNameResponse(val userName: String)

data class TokenResponse(
    val appId: String,
    val tokens: Tokens,
    val uid: String
)

data class Tokens(
    val rtmToken: String,
    val rtcToken: String
)

interface ApiService {
    @GET("getToken")
    fun getUsers(
        @Query("userId") userId: String,
        @Query("channelName") channelName: String
    ): Call<TokenResponse>

    @GET("getUserName")
    fun getUserName(
        @Query("uid") uid: String,
    ): Call<UserNameResponse>


}