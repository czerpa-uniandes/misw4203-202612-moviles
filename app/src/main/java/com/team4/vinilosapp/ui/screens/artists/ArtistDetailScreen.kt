package com.team4.vinilosapp.ui.screens.artists

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.team4.vinilosapp.data.models.AlbumReference
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.PerformerPrize
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.viewmodels.ArtistViewModel

private val VinilosPrimary = Color(0xFFB4532A)
private val ScreenBg = Color(0xFFF7F4F2)
private val SecondaryText = Color(0xFF5F4D46)
private val CardBg = Color(0xFFF1F0EF)

@Composable
fun ArtistDetailScreen(
    navController: NavController,
    artistId: Int,
    viewModel: ArtistViewModel = viewModel()
) {
    val artist by viewModel.selectedArtist.collectAsState()
    val isLoading by viewModel.detailLoading.collectAsState()
    val error by viewModel.detailError.collectAsState()

    LaunchedEffect(artistId) {
        viewModel.fetchArtistDetail(artistId)
    }

    Scaffold(
        containerColor = ScreenBg,
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator(color = VinilosPrimary) }

            error != null -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(text = error ?: "Error", color = SecondaryText) }

            artist != null -> ArtistDetailContent(
                navController = navController,
                artist = artist!!,
                paddingValues = padding
            )
        }
    }
}

@Composable
private fun ArtistDetailContent(
    navController: NavController,
    artist: ArtistDetail,
    paddingValues: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .testTag("artist_detail_scroll"),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
            ArtistHeader(navController = navController, artist = artist)
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "El Centurión de la Noche",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF202020),
                    modifier = Modifier.testTag("artist_detail_section_biography")
                )
                Text(
                    text = artist.description.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = SecondaryText,
                    modifier = Modifier.testTag("artist_detail_description")
                )
                InfoPanel(artist)
            }
        }

        item {
            SectionHeader(
                title = "Discografía en Catálogo",
                subtitle = "Joyas prensadas disponibles para tu colección",
                modifier = Modifier.testTag("artist_detail_albums_section")
            )
        }

        if (artist.albums.isEmpty()) {
            item { EmptyText("No hay álbumes asociados todavía.") }
        } else {
            items(artist.albums.chunked(2)) { rowAlbums ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    rowAlbums.forEach { album ->
                        AlbumMiniCard(album = album, modifier = Modifier.weight(1f))
                    }
                    if (rowAlbums.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        item {
            SectionHeader(
                title = "Integrantes: La Verdad",
                modifier = Modifier.testTag("artist_detail_bands_section")
            )
        }

        if (artist.bands.isEmpty()) {
            item { EmptyText("No hay bandas asociadas todavía.") }
        } else {
            items(artist.bands) { band -> BandRow(band.name) }
        }

        item {
            SectionHeader(
                title = "Premios y Reconocimientos",
                modifier = Modifier.testTag("artist_detail_prizes_section")
            )
        }

        if (artist.performerPrizes.isEmpty()) {
            item { EmptyText("No hay premios asociados todavía.") }
        } else {
            items(artist.performerPrizes) { prize -> PrizeCard(prize) }
        }

        item {
            Button(
                onClick = {
                    navController.navigate("associate_prize_artist/${artist.id}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VinilosPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 8.dp)
                    .testTag("assosiate_prize_artist_button"),
            ) {
                Text("Agregar Premio", color = Color.White)
            }
        }
    }
}

@Composable
private fun ArtistHeader(navController: NavController, artist: ArtistDetail) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(artist.image).crossfade(true).build(),
            contentDescription = artist.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().testTag("artist_detail_image"),
            loading = { Box(Modifier.fillMaxSize().background(CardBg)) },
            error = {
                Box(Modifier.fillMaxSize().background(CardBg), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = VinilosPrimary, modifier = Modifier.size(96.dp))
                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, ScreenBg), startY = 240f))
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 10.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = VinilosPrimary)
            }
            Text("Vinilos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = VinilosPrimary)
            Spacer(Modifier.weight(1f))
            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = VinilosPrimary)
        }
        Column(
            modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 24.dp, vertical = 34.dp)
        ) {
            Text("LEYENDA DE LA SALSA", color = VinilosPrimary, fontWeight = FontWeight.Bold, letterSpacing = MaterialTheme.typography.labelLarge.letterSpacing)
            Text(
                text = artist.name,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1F1F1F),
                modifier = Modifier.testTag("artist_detail_name")
            )
        }
    }
}

@Composable
private fun InfoPanel(artist: ArtistDetail) {
    Surface(color = CardBg, shape = RoundedCornerShape(26.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = VinilosPrimary)
                Spacer(Modifier.width(12.dp))
                Text("Cartagena, Colombia", fontWeight = FontWeight.Bold, modifier = Modifier.testTag("artist_detail_location"))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = VinilosPrimary)
                Spacer(Modifier.width(12.dp))
                Text(formatLifeDates(artist.birthDate), fontWeight = FontWeight.Bold, modifier = Modifier.testTag("artist_detail_birth_date"))
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 8.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = Color(0xFF202020))
        if (subtitle != null) Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
    }
}

@Composable
private fun AlbumMiniCard(album: AlbumReference, modifier: Modifier = Modifier) {
    Column(modifier = modifier.testTag("artist_detail_album_item")) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(album.cover).crossfade(true).build(),
            contentDescription = album.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(24.dp)),
            loading = { Box(Modifier.fillMaxSize().background(CardBg)) },
            error = { Box(Modifier.fillMaxSize().background(CardBg)) }
        )
        Spacer(Modifier.height(8.dp))
        Text(album.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(album.recordLabel, color = SecondaryText, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun BandRow(name: String) {
    Text(
        text = name,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp).testTag("artist_detail_band_item")
    )
}

@Composable
private fun PrizeCard(performerPrize: PerformerPrize) {
    Surface(
        color = CardBg,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp).testTag("artist_detail_prize_item")
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFEADFD9)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = VinilosPrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(performerPrize.prize?.name ?: "Premio", fontWeight = FontWeight.ExtraBold)
                Text(performerPrize.prize?.description ?: performerPrize.premiationDate.orEmpty(), color = SecondaryText)
            }
        }
    }
}

@Composable
private fun EmptyText(text: String) {
    Text(text = text, color = SecondaryText, modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
}

private fun formatLifeDates(birthDate: String?): String {
    val year = birthDate?.take(4)?.takeIf { it.all(Char::isDigit) } ?: "Fecha no disponible"
    return if (year == "Fecha no disponible") year else "$year"
}
