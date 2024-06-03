package com.example.meetapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
        requestCameraAndMicrophonePermissions()
        enableEdgeToEdge()

        setContent {
            if (!loginVM.isTokenSuccess) {
                Toast.makeText(this, "Error While logging in, Please try again", Toast.LENGTH_LONG)
                    .show()
                loginVM.setTokenSuccess(true)
            }
            Box(modifier = Modifier.padding(50.dp)) {
                LoginScreen() { it1, it2 ->
                    loginVM.getAgoraToken(it1, it2, navigateToMeet)
                }
            }
        }
    }
    private fun requestCameraAndMicrophonePermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val microphonePermission = Manifest.permission.RECORD_AUDIO

        // Check if the permissions are already granted
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val microphonePermissionGranted = ContextCompat.checkSelfPermission(this, microphonePermission) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermissionGranted || !microphonePermissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(cameraPermission, microphonePermission),
                123
            )
        }
    }


}