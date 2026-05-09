package com.team4.vinilosapp.ui.screens.artists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel
import com.team4.vinilosapp.ui.viewmodels.ArtistViewModel

private val VinilosPrimary = Color(0xFFB4532A)
private val ScreenBg = Color(0xFFF7F4F2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlbumToArtistScreen(
    navController: NavController,
    artistId: Int,
    albumViewModel: AlbumViewModel = viewModel()
) {
    val artistViewModel: ArtistViewModel = viewModel(
        navController.previousBackStackEntry!!
    )

    val albums by albumViewModel.albums.collectAsState()
    val isLoadingAlbums by albumViewModel.isLoading.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var selectedAlbumId by remember { mutableStateOf<String?>(null) }
    var selectedAlbumName by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        albumViewModel.fetchAlbums()
    }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Asociar álbum al artista",
                        fontWeight = FontWeight.Bold,
                        color = VinilosPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = VinilosPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ScreenBg)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona el álbum a asociar",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202020)
            )

            when {
                isLoadingAlbums -> Box(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = VinilosPrimary)
                }

                albums.isEmpty() -> Text(
                    text = "No hay álbumes disponibles.",
                    color = Color(0xFF5F4D46)
                )

                else -> ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.testTag("album_dropdown_box")
                ) {
                    OutlinedTextField(
                        value = selectedAlbumName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Álbum") },
                        placeholder = { Text("Elige un álbum") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = VinilosPrimary,
                            unfocusedBorderColor = Color.Gray,
                            focusedLabelColor = VinilosPrimary,
                            cursorColor = VinilosPrimary
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("album_dropdown_input")
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        albums.forEach { album ->
                            DropdownMenuItem(
                                text = { Text(album.name) },
                                onClick = {
                                    selectedAlbumId = album.id
                                    selectedAlbumName = album.name
                                    expanded = false
                                },
                                modifier = Modifier.testTag("album_option_${album.id}")
                            )
                        }
                    }
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red)
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val albumId = selectedAlbumId?.toIntOrNull() ?: return@Button
                    isSubmitting = true
                    errorMessage = null
                    artistViewModel.addAlbumToArtist(
                        artistId = artistId,
                        albumId = albumId,
                        onSuccess = {
                            isSubmitting = false
                            artistViewModel.fetchArtistDetail(artistId)
                            navController.popBackStack()
                        },
                        onError = {
                            isSubmitting = false
                            errorMessage = "No se pudo asociar el álbum. Intenta de nuevo."
                        }
                    )
                },
                enabled = !isSubmitting && selectedAlbumId != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("associate_album_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VinilosPrimary,
                    disabledContainerColor = Color.LightGray
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Asociar álbum", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
