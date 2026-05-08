package com.team4.vinilosapp.ui.screens.prizes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.team4.vinilosapp.data.models.Prize
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.viewmodels.PrizeViewModel


private val Primary = Color(0xFF9D3E1D)
@Composable
fun PrizesScreen(
    navController: NavController,
    viewModel: PrizeViewModel = viewModel()
) {
    val prizes by viewModel.prizes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPrizes()
    }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNav(navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_prize") },
                containerColor = Color(0xFFB4532A),
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                },
                text = {
                    Text("Añadir Premio", color = Color.White)
                },
                modifier = Modifier.testTag("bottom_add_prizes")
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFFB4532A)
                    )
                }

                error != null -> {
                    Text(
                        text = error ?: "Ocurrió un error",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    PrizesContent(prizes = prizes)
                }
            }
        }
    }
}

@Composable
fun PrizesContent(prizes: List<Prize>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Premios",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                color = Primary
            )
        }

        if (prizes.isEmpty()) {
            item {
                Text(
                    text = "No hay premios disponibles.",
                    color = Color.Gray
                )
            }
        } else {
            items(prizes) { prize ->
                PrizeItem(prize = prize)
            }
        }
    }
}

@Composable
fun PrizeItem(prize: Prize) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F7F7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = prize.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = prize.organization,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB4532A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = prize.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Premiaciones: ${prize.performerPrizes.size}",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}