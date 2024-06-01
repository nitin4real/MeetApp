package com.example.meetapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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

        setContent {
            if(!loginVM.isTokenSuccess){
                Toast.makeText(this,"Error While logging in, Please try again", Toast.LENGTH_LONG).show()
                loginVM.setTokenSuccess(true)
            }
            Box(modifier = Modifier.padding(50.dp)) {
                UserInputScreen() { it1, it2 ->
                    loginVM.getAgoraToken(it1, it2, navigateToMeet)
                }
            }
        }
    }
}

@Composable
fun UserInputScreen(onSubmit: (String, String) -> Unit) {
    var username by remember { mutableStateOf("") }
    var channelName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = channelName,
            onValueChange = { channelName = it },
            label = { Text("Channel Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSubmit(username, channelName) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}