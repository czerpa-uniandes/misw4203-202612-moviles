package com.team4.vinilosapp.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArtistList() {

    Column {

        ArtistItem("Fela Kuti", "12 LPs")
        ArtistItem("David Bowie", "26 LPs")
        ArtistItem("Charly García", "18 LPs")
    }
}

@Composable
fun ArtistItem(name: String, albums: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column {
            Text(name, style = MaterialTheme.typography.titleMedium)
            Text(albums, style = MaterialTheme.typography.bodySmall)
        }
    }
}