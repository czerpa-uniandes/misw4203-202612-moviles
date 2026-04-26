package com.team4.vinilosapp.ui.screens.albums

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.team4.vinilosapp.ui.screens.albums.components.AlbumTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team4.vinilosapp.ui.components.BottomNav
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.ui.screens.albums.components.AddTrackAlbumTopBar
import com.team4.vinilosapp.ui.viewmodels.AlbumDetailUiState
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel

private val VinilosPrimary = Color(0xFFB44A1F)
private val ScreenBg = Color(0xFFF7F4F2)

@Composable
fun AddTrackAlbumScreen(
    navController: NavController,
    albumId: Int
) {
    val viewModel: AlbumViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var name by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }

    val createLoading by viewModel.createLoading.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()

    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    Scaffold(
        containerColor = ScreenBg,
        topBar = { AddTrackAlbumTopBar(navController) },
        bottomBar = { BottomNav(navController) }
    ) { padding ->

        when (val state = uiState) {

            is AlbumDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AlbumDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${state.message}")
                }
            }

            is AlbumDetailUiState.Success -> {

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        AddTrackHeader(album = state.album)
                    }

                    item {
                        TrackTextField(
                            "Nombre de la canción",
                            "Ej. El cantante",
                            name,
                            modifier = Modifier.testTag("track_name_input").fillMaxWidth()
                            ) { name = it }
                    }

                    item {
                        Column {

                            Text(
                                text = "Duración",
                                style = MaterialTheme.typography.labelMedium,
                                color = VinilosPrimary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    TrackNumberField(
                                        value = minutes,
                                        modifier = Modifier.width(80.dp).testTag("track_minutes_input"),
                                        placeholder = "Min",
                                        onChange = { minutes = it }
                                    )

                                    Text(
                                        ":",
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    TrackNumberField(
                                        value = seconds,
                                        modifier = Modifier.width(80.dp).testTag("track_seconds_input"),
                                        placeholder = "Seg",
                                        onChange = { seconds = it }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = {
                                val duration = "${minutes}:${seconds}"

                                viewModel.addTrack(
                                    albumId = albumId,
                                    name = name,
                                    duration = duration,
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 24.dp)
                                .testTag("add_track_button"),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = VinilosPrimary,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.dp, VinilosPrimary)
                        ) {
                            Text("Añadir canción")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddTrackHeader(album: Album) {

    val artistName = album.performers.firstOrNull()?.name ?: "Artista desconocido"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Imagen
        AsyncImage(
            model = album.cover,
            contentDescription = album.name,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EDITANDO ÁLBUM",
                color = VinilosPrimary
            )

            Text(
                text = album.name,
                modifier = Modifier.testTag("add_track_album_title"),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = artistName,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun TrackNumberField(
    value: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            // opcional: solo números
            if (it.all { char -> char.isDigit() }) {
                onChange(it)
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number // Equivalente a "type=number" de HTML
        ),
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = VinilosPrimary,
            cursorColor = VinilosPrimary
        )
    )
}

@Composable
fun TrackTextField(
    label: String,
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit
) {

    Column(modifier = Modifier.padding(vertical = 8.dp)) {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFFB4532A),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = { Text(placeholder) },
            modifier = modifier,
            shape = RoundedCornerShape(50),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFB4532A),
                cursorColor = Color(0xFFB4532A)
            )
        )
    }
}