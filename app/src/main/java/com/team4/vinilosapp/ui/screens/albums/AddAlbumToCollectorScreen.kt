package com.team4.vinilosapp.ui.screens.albums

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
@Composable
fun AddAlbumToCollectorScreen(
    navController: NavController,
    albumId: String,
    viewModel: AlbumViewModel = viewModel()
) {
    var price by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Active") }
    var expanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agregar a mi colección",
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
                text = "Completa la información del álbum",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = price,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        price = it
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VinilosPrimary,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = VinilosPrimary,
                    cursorColor = VinilosPrimary
                ),
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
                    .testTag("price_input")
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = if (status == "Active") "Activo" else "Inactivo",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
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
                        .testTag("status_input")
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Activo") },
                        onClick = {
                            status = "Active"
                            expanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Inactivo") },
                        onClick = {
                            status = "Inactive"
                            expanded = false
                        }
                    )
                }
            }

            message?.let {
                Text(
                    text = it,
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val priceValue = price.toIntOrNull()

                    if (priceValue == null || priceValue <= 0) {
                        message = "Ingresa un precio válido"
                        return@Button
                    }

                    viewModel.addAlbumToCollector(
                        collectorId = "100",
                        albumId = albumId,
                        price = priceValue,
                        status = status,
                        onSuccess = {
                            navController.popBackStack()
                        },
                        onError = {
                            message = "No se pudo agregar el álbum"
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("add_album_collector_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VinilosPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar a colección")
            }
        }
    }
}