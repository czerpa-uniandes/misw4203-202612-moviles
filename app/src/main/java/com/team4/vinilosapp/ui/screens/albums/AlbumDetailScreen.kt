package com.team4.vinilosapp.ui.screens.albums

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Comment
import com.team4.vinilosapp.data.models.Track
import com.team4.vinilosapp.navigation.Screen
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.viewmodels.AlbumDetailUiState
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel
import java.time.OffsetDateTime

private val VinilosPrimary = Color(0xFFB44A1F)
private val VinilosTeal = Color(0xFF0C9A9A)
private val ChipBg = Color(0xFFF1ECE8)
private val ScreenBg = Color(0xFFF7F4F2)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlbumDetailScreen(
    navController: NavController,
    albumId: Int,
    sectionTitle: String,
    viewModel: AlbumViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(albumId) {
        viewModel.loadAlbum(albumId)
    }

    Scaffold(
        containerColor = ScreenBg,
        bottomBar = { BottomNav(navController) }
    ) { paddingValues ->
        when (val state = uiState) {
            is AlbumDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AlbumDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message)
                }
            }

            is AlbumDetailUiState.Success -> {
                AlbumDetailContent(
                    navController = navController,
                    album = state.album,
                    sectionTitle = sectionTitle,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AlbumDetailContent(
    navController: NavController,
    album: Album,
    sectionTitle: String,
    paddingValues: PaddingValues
) {
    val artistName = album.performers.firstOrNull()?.name ?: "Artista desconocido"
    val year = formatYear(album.releaseDate)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .padding(paddingValues),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            AlbumTopBar(
                title = sectionTitle,
                onBack = { navController.popBackStack() }
            )
        }

        item {
            AsyncImage(
                model = album.cover,
                contentDescription = album.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .testTag("album_detail_cover"),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = album.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1C1C1C),
                    modifier = Modifier.testTag("album_detail_title")
                )

                Text(
                    text = artistName.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = VinilosPrimary,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(
                        text = year,
                        modifier = Modifier.testTag("album_detail_release_date")
                    )
                    InfoChip(
                        text = album.genre.uppercase(),
                        modifier = Modifier.testTag("album_detail_genre")
                    )
                    InfoChip(
                        text = album.recordLabel.uppercase(),
                        modifier = Modifier.testTag("album_detail_label")
                    )
                    BadgeChip("CURADOR PRO", VinilosTeal)
                }
            }
        }

        item {
            Text(
                text = album.description,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6D625C),
                modifier = Modifier.testTag("album_detail_description")
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VinilosPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Añadir a mi colección")
                }
            }
        }

        item {
            SectionTitle("Lista de canciones")
        }

        items(album.tracks.size) { index ->
            TrackRow(index = index + 1, track = album.tracks[index])
        }

        item {
            OutlinedButton(
                onClick = { navController.navigate(Screen.AddTrackAlbum.createRoute(albumId = album.id)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = VinilosPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Añadir canción",
                    color = VinilosPrimary
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle("Coleccionistas")
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Ver todos",
                    color = VinilosPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        if (album.comments.isEmpty()) {
            item {
                Text(
                    text = "No hay comentarios todavía.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            items(album.comments.size) { index ->
                CommentCard(comment = album.comments[index], index = index)
            }
        }
    }
}

@Composable
private fun AlbumTopBar(
    title: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = VinilosPrimary
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = VinilosPrimary,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFF111111)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "👤",
                color = Color.White
            )
        }
    }
}

@Composable
private fun InfoChip(
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = ChipBg,
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF6D625C)
        )
    }
}

@Composable
private fun BadgeChip(text: String, color: Color) {
    Surface(
        color = color,
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        HorizontalDivider(
            modifier = Modifier.width(32.dp),
            thickness = 2.dp,
            color = VinilosPrimary
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1C1C1C)
        )
    }
}

@Composable
private fun TrackRow(index: Int, track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = index.toString().padStart(2, '0'),
            modifier = Modifier.width(36.dp),
            color = Color(0xFFD7B1A4),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF222222)
            )
            Text(
                text = "Lado ${if (index % 2 == 0) "B" else "A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFA99A93)
            )
        }

        Text(
            text = track.duration,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF5E524D)
        )
    }
}

@Composable
private fun CommentCard(comment: Comment, index: Int) {
    val names = listOf("Mateo Valencia", "Elena Restrepo", "Laura Pineda", "Camilo Vélez")
    val avatars = listOf("🧑🏻", "👩🏻", "👩🏽", "🧔🏻")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8F6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD8ECEB)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(avatars[index % avatars.size])
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = names[index % names.size],
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B2B2B)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        repeat(comment.rating) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB39B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "\"${comment.description}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6A5E58)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatYear(date: String): String {
    return try {
        OffsetDateTime.parse(date).year.toString()
    } catch (_: Exception) {
        date.take(4)
    }
}