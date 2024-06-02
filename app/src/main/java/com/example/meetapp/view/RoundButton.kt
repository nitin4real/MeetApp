package com.example.meetapp.view

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun RoundButton(image: Int, imageTint: Color, cb: () -> Unit) {
    IconButton(
        onClick = { cb() },
        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.size(56.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(image),
            contentDescription = "Action",
            tint = imageTint,
            modifier = Modifier.size(30.dp)
        )
    }
}