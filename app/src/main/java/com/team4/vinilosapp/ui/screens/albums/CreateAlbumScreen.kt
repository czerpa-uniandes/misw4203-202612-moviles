package com.team4.vinilosapp.ui.screens.albums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.collectAsState
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel
import com.team4.vinilosapp.ui.screens.albums.components.AlbumTextField
import com.team4.vinilosapp.ui.screens.albums.components.CreateAlbumButton
import com.team4.vinilosapp.ui.screens.albums.components.CreateAlbumTopBar
import com.team4.vinilosapp.ui.screens.albums.components.ChipSelector
import com.team4.vinilosapp.ui.screens.albums.components.DescriptionField
import com.team4.vinilosapp.ui.screens.albums.components.ReleaseDateField


@Composable
fun CreateAlbumScreen(navController: NavController) {
    val viewModel: AlbumViewModel = viewModel()
    var title by remember { mutableStateOf("") }
//    var cover by remember { mutableStateOf<Uri?>(null) }
    var cover by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var releaseDate by remember { mutableStateOf("") }
    var recordLabel by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        cover = uri
//    }

    val createLoading by viewModel.createLoading.collectAsState()
    val createSuccess by viewModel.createSuccess.collectAsState()

    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { CreateAlbumTopBar(navController) },
        bottomBar = { CreateAlbumButton(
                isLoading = createLoading,
                onClick = {
                    viewModel.createAlbum(
                        name = title,
                        cover = cover ?: "",
                        releaseDate = releaseDate,
                        description = description,
                        genre = genre,
                        recordLabel = recordLabel
                    )
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
//            item { CoverUploader(
//                imageUri = cover,
//                onClick = {
//                    imagePickerLauncher.launch("image/*")
//                })
//            }

            item { AlbumTextField("TÍTULO DEL ÁLBUM", "Ej: Artaud", title) { title = it } }

            item { AlbumTextField("URL DE CARATULA", "Ej: https://image.url", cover) { cover = it } }

            item {
                ReleaseDateField(
                    value = releaseDate,
                    onDateSelected = {
                        releaseDate = it
                    }
                )
            }

            item {
                ChipSelector(
                    label = "SELLO DISCOGRÁFICO",
                    options = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records"),
                    selectedOption = recordLabel,
                    onOptionSelected = { recordLabel = it }
                )
            }

            item {
                ChipSelector(
                    label = "GENERO",
                    options = listOf("Classical", "Salsa", "Rock", "Folk"),
                    selectedOption = genre,
                    onOptionSelected = { genre = it }
                )
            }

            item { DescriptionField(description) { description = it } }

            item { Spacer(modifier = Modifier.height(120.dp)) }
        }
    }
}
