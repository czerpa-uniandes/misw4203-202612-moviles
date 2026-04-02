package com.team4.vinilosapp.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FeaturedArtistCard() {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {

        Column {

            AsyncImage(
                model = "https://via.placeholder.com/600",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text("Joe Arroyo", style = MaterialTheme.typography.titleLarge)
                Text("14 LPs", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}