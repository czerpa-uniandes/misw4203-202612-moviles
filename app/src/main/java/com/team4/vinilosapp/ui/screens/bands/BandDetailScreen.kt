package com.team4.vinilosapp.ui.screens.bands

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.viewmodels.BandViewModel

private val Primary = Color(0xFF2A6DB4)
private val SecondaryText = Color(0xFF5F5E5C)
private val Background = Color(0xFFF8F6F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandDetailScreen(
    navController: NavController,
    bandId: Int
) {
    val viewModel: BandViewModel = viewModel()
    val band by viewModel.selectedBand.collectAsState()
    val isLoading by viewModel.detailLoading.collectAsState()
    val error by viewModel.detailError.collectAsState()

    LaunchedEffect(bandId) {
        viewModel.fetchBandDetail(bandId)
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = { Text(text = band?.name ?: "Detalle de banda") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Primary
                )
            )
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error ?: "Error al cargar la banda", color = Color.Gray)
                }
            }

            band != null -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
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
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(band!!.image)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = band!!.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(CircleShape),
                                    loading = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Primary.copy(alpha = 0.12f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(28.dp),
                                                color = Primary,
                                                strokeWidth = 2.dp
                                            )
                                        }
                                    },
                                    error = {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Primary.copy(alpha = 0.12f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Group,
                                                contentDescription = null,
                                                tint = Primary,
                                                modifier = Modifier.size(52.dp)
                                            )
                                        }
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = band!!.name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Primary
                                )

                                if (!band!!.creationDate.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Fundada: ${band!!.creationDate!!.take(10)}",
                                        fontSize = 14.sp,
                                        color = SecondaryText
                                    )
                                }

                                if (!band!!.description.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = Color.LightGray)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = band!!.description!!,
                                        fontSize = 14.sp,
                                        color = SecondaryText,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }
                    }

                    if (band!!.musicians.isNotEmpty()) {
                        item {
                            Text(
                                text = "Integrantes",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Primary,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }

                        items(band!!.musicians) { musician ->
                            MusicianRow(musician = musician)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicianRow(musician: Performer) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(musician.image)
                    .crossfade(true)
                    .build(),
                contentDescription = musician.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFEEEEED)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Primary,
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFEEEEED)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = musician.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                if (!musician.birthDate.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Nacimiento: ${musician.birthDate!!.take(10)}",
                        fontSize = 13.sp,
                        color = SecondaryText
                    )
                }
            }
        }
    }
}
