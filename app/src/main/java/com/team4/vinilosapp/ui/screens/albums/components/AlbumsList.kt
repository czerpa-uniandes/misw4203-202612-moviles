package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel

@Composable
fun AlbumsList(navController: NavController){

    val viewModel: AlbumViewModel = viewModel()
    val albums by viewModel.albums.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAlbums()
    }

    Column(modifier = Modifier.testTag("albums_list_container")) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Lista de Albumes",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFFB4532A))
                }
            }

            albums.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay álbumes disponibles",
                        color = Color.Gray
                    )
                }
            }

            else -> {
                albums.forEach { album ->
                    AlbumCard(
                        id = album.id,
                        title = album.name,
                        artist = album.performers.firstOrNull()?.name ?: "Artista desconocido",
                        imageUrl = album.cover,
                        navController = navController
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}