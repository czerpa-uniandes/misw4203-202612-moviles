package com.team4.vinilosapp.ui.screens.albums

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel

private val VinilosPrimary = Color(0xFFB44A1F)
private val ScreenBg = Color(0xFFF7F4F2)

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CommentAlbumScreen(
    navController: NavController,
    albumId: String,
    viewModel: AlbumViewModel = viewModel()
) {
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Comentar álbum",
                        fontWeight = FontWeight.Bold,
                        color = VinilosPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = VinilosPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ScreenBg
                )
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
                text = "Comparte tu opinión sobre este álbum",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Comentario") },
                modifier = Modifier.fillMaxWidth()
                    .testTag("comment_input"),
                minLines = 4
            )

            OutlinedTextField(
                value = rating,
                onValueChange = { value ->
                    if (value.all { it.isDigit() }) {
                        rating = value
                    }
                },
                label = { Text("Calificación de 1 a 5") },
                modifier = Modifier.fillMaxWidth()
                    .testTag("rating_input"),
                singleLine = true
            )

            message?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val ratingValue = rating.toIntOrNull()

                    if (comment.isBlank()) {
                        message = "Debes escribir un comentario"
                        return@Button
                    }

                    if (ratingValue == null || ratingValue !in 1..5) {
                        message = "La calificación debe estar entre 1 y 5"
                        return@Button
                    }

                    viewModel.addComment(
                        albumId = albumId,
                        collectorId = 100,
                        description = comment,
                        rating = ratingValue,
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = {
                            message = "No se pudo agregar el comentario"
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("send_comment_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VinilosPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enviar comentario")
            }
        }
    }
}