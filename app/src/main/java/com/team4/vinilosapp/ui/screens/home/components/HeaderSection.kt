package com.team4.vinilosapp.ui.screens.home.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.padding(bottom = 24.dp)) {

        Text(
            text = "Artistas que definen eras",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explora el legado sonoro...",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}