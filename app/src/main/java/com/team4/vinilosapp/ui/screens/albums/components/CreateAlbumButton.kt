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
import androidx.compose.ui.unit.dp

@Composable
fun CreateAlbumButton(isLoading: Boolean = false, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB4532A)
        ),
        shape = RoundedCornerShape(50)
    ) {
        Icon(Icons.Default.Add, contentDescription = null)

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if(isLoading) "Creando..." else "Crear Album",
            color = Color.White
        )
    }
}