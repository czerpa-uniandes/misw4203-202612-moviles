package com.team4.vinilosapp.ui.screens.collectors.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.viewmodels.CollectorViewModel

@Composable
fun CollectorsList(navController: NavController) {
    val viewModel: CollectorViewModel = viewModel()
    val collectors by viewModel.collectors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCollectors()
    }

    Column {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Directorio",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
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
                    CircularProgressIndicator(color = Color(0xFF9D3E1D))
                }
            }
            collectors.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay coleccionistas disponibles", color = Color.Gray)
                }
            }
            else -> {
                collectors.forEach { collector ->
                    CollectorCard(collector = collector, navController = navController)
                }
            }
        }
    }
}
