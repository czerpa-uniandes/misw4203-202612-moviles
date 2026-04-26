package com.team4.vinilosapp.ui.screens.collectors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.ui.screens.collectors.components.CollectorDetailTopBar
import com.team4.vinilosapp.ui.screens.collectors.components.CollectorInfoRow
import com.team4.vinilosapp.ui.screens.collectors.components.CollectorSectionTitle
import com.team4.vinilosapp.ui.screens.collectors.components.CollectorSimpleInfoCard
import com.team4.vinilosapp.ui.viewmodels.CollectorViewModel

private val Primary = Color(0xFF9D3E1D)
private val SecondaryText = Color(0xFF5F5E5C)
private val Background = Color(0xFFF8F6F5)

@Composable
fun CollectorDetailScreen(
    navController: NavController,
    collectorId: Int
) {
    val viewModel: CollectorViewModel = viewModel()
    val collector by viewModel.selectedCollector.collectAsState()
    val isLoading by viewModel.detailLoading.collectAsState()
    val error by viewModel.detailError.collectAsState()

    LaunchedEffect(collectorId) {
        viewModel.fetchCollectorById(collectorId)
    }

    Scaffold(
        containerColor = Background,
        topBar = { CollectorDetailTopBar(navController) },
    ) { padding ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error ?: "Error al cargar coleccionista")
                }
            }

            collector != null -> {
                CollectorDetailContent(
                    padding = padding,
                    collector = collector!!,
                    navController = navController
                )
            }
        }
    }
}

@Composable
private fun CollectorDetailContent(
    padding: PaddingValues,
    collector: CollectorDetail,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .testTag("collector_detail_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .background(Primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = collector.name,
                        modifier = Modifier.testTag("collector_detail_name"),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CollectorInfoRow(
                        icon = Icons.Default.Email,
                        text = collector.email
                    )

                    CollectorInfoRow(
                        icon = Icons.Default.Phone,
                        text = collector.telephone
                    )
                }
            }
        }

        item {
            CollectorSectionTitle("Comentarios")
        }

        items(collector.comments) { comment ->
            CollectorSimpleInfoCard {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = comment.description,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Rating: ${comment.rating}/5",
                            color = SecondaryText,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        item {
            CollectorSectionTitle("Artistas favoritos")
        }

        items(collector.favoritePerformers) { performer ->
            CollectorSimpleInfoCard {
                Text(
                    text = performer.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = performer.description,
                    color = SecondaryText,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Nacimiento: ${performer.birthDate?.take(10) ?: "Sin fecha"}",
                    color = Primary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item {
            CollectorSectionTitle("Álbumes del coleccionista")
        }

        items(collector.collectorAlbums) { album ->
            CollectorSimpleInfoCard {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                    navController.navigate("albumDetail/${album.id}/Lista de canciones")
                }) {
                    Icon(
                        imageVector = Icons.Default.Album,
                        contentDescription = null,
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Álbum #${album.id}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Precio: $${album.price} - Estado: ${album.status}",
                            color = SecondaryText,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

