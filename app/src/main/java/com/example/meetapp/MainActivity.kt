package com.example.meetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.meetapp.controller.Configs
import com.example.meetapp.controller.LoginViewModel
import com.example.meetapp.view.MeetActivity
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    private val loginVM: LoginViewModel by viewModels()

    private val navigateToMeet = fun(configs: Configs) {
        val intent = Intent(this, MeetActivity::class.java).apply {
            val gson = Gson()
            val configsJson = gson.toJson(configs)
            putExtra("configs", configsJson)
        }
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginVM.getAgoraToken("123", "123", navigateToMeet)

        setContent {
            if(!loginVM.isTokenSuccess){
                Toast.makeText(this,"Error While logging in, Please try again", Toast.LENGTH_LONG).show()
                loginVM.setTokenSuccess(true)
            }
            Box(modifier = Modifier.padding(50.dp)) {
                LoginScreen() { it1, it2 ->
                    loginVM.getAgoraToken(it1, it2, navigateToMeet)
                }
            }
        }
    }
}

