package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.team4.vinilosapp.ui.models.AlbumFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    onApply: (AlbumFilter) -> Unit
) {

    var query by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }

    ModalBottomSheet(
        containerColor = Color.White,
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Filtros", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB4532A),
                    focusedLabelColor = Color(0xFFB4532A),
                    cursorColor = Color(0xFFB4532A)
                ),
                label = { Text("Buscar álbum") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = genre,
                onValueChange = { genre = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB4532A),
                    focusedLabelColor = Color(0xFFB4532A),
                    cursorColor = Color(0xFFB4532A)
                ),
                label = { Text("Género") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFB4532A),
                    focusedLabelColor = Color(0xFFB4532A),
                    cursorColor = Color(0xFFB4532A)
                ),
                label = { Text("Artista o banda") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onApply(
                        AlbumFilter(
                            query = query,
                            genre = genre,
                            artist = artist
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB4532A)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Aplicar filtros",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}