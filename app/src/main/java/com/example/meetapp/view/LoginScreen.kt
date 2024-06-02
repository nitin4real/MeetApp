package com.example.meetapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.meetapp.ui.theme.CustomColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onSubmit: (String, String) -> Unit) {
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
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black, // Cursor color
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = CustomColors.secondary,
                focusedLabelColor = CustomColors.primary
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = channelName,
            onValueChange = { channelName = it },
            label = { Text("Channel Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black, // Cursor color
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = CustomColors.secondary,
                focusedLabelColor = CustomColors.primary
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSubmit(username, channelName) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomColors.primary
            )
        ) {
            Text("Submit")
        }
    }
}