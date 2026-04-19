package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddAlbumButton(onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB4532A)
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(48.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Añadir",
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Añadir álbum",
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}