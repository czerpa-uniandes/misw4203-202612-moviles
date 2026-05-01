package com.team4.vinilosapp.ui.screens.bands.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4.vinilosapp.ui.viewmodels.BandViewModel

@Composable
fun BandsList(onBandClick: (Int) -> Unit = {}) {
    val viewModel: BandViewModel = viewModel()
    val bands by viewModel.bands.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchBands()
    }

    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Lista de Bandas",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2A6DB4))
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = error!!, color = Color.Gray, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { viewModel.fetchBands() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A6DB4))
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            bands.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay bandas disponibles", color = Color.Gray)
                }
            }
            else -> {
                bands.forEach { band ->
                    BandCard(band = band, onClick = { onBandClick(band.id) })
                }
            }
        }
    }
}
