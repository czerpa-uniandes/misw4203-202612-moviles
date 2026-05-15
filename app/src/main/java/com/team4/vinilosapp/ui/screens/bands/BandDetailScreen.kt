package com.team4.vinilosapp.ui.screens.bands

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.viewmodels.BandViewModel

private val VinilosPrimary  = Color(0xFFB4532A)
private val ScreenBg        = Color(0xFFF7F4F2)
private val CardBg          = Color(0xFFF1ECE8)
private val SecondaryText   = Color(0xFF4A403A)
private val PlaceholderBg   = Color(0xFFEEEEED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BandDetailScreen(
    navController: NavController,
    bandId: Int
) {
    val viewModel: BandViewModel = viewModel()
    val band        by viewModel.selectedBand.collectAsState()
    val isLoading   by viewModel.detailLoading.collectAsState()
    val error       by viewModel.detailError.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(bandId) { viewModel.fetchBandDetail(bandId) }

    Scaffold(
        containerColor = ScreenBg,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = band?.name ?: "Detalle de banda",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = VinilosPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = VinilosPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = VinilosPrimary
                )
            )
        },
        floatingActionButton = {
            if (band != null) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.fetchAllMusicians()
                        showSheet = true
                    },
                    containerColor = VinilosPrimary,
                    contentColor = Color.White,
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    text = { Text("Asociar músico", fontWeight = FontWeight.SemiBold) },
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    ) { padding ->
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = VinilosPrimary) }
            }

            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = error ?: "Error al cargar la banda",
                        color = SecondaryText,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            band != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item { BandHeaderCard() }
                    item { BandSectionTitle("Músicos") }

                    if (band!!.musicians.isEmpty()) {
                        item {
                            Text(
                                text = "Sin músicos asociados aún.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = SecondaryText,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    } else {
                        items(band!!.musicians) { musician ->
                            MusicianRow(musician = musician)
                        }
                    }

                    item { Spacer(modifier = Modifier.height(72.dp)) }
                }
            }
        }
    }

    if (showSheet) {
        AddMusicianSheet(
            sheetState    = sheetState,
            bandId        = bandId,
            currentMusicianIds = band?.musicians?.map { it.id }?.toSet() ?: emptySet(),
            viewModel     = viewModel,
            onDismiss     = { showSheet = false }
        )
    }
}

@Composable
private fun BandHeaderCard() {
    val viewModel: BandViewModel = viewModel()
    val band by viewModel.selectedBand.collectAsState()

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(band!!.image)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            color = VinilosPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = VinilosPrimary,
                            modifier = Modifier.size(52.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = band!!.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1C1C1C),
                modifier = Modifier.semantics {
                    contentDescription = "Nombre de la banda: ${band!!.name}"
                }
            )

            if (!band!!.creationDate.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    color = Color(0xFFF1ECE8),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "Fundada ${band!!.creationDate!!.take(10)}",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = SecondaryText
                    )
                }
            }

            if (!band!!.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = Color(0xFFEEEEEE))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = band!!.description!!,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SecondaryText,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                )
            }
        }
    }
}

@Composable
private fun BandSectionTitle(text: String) {
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
private fun MusicianRow(musician: Performer) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
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
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = VinilosPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = VinilosPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = musician.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1C1C1C),
                    modifier = Modifier.semantics {
                        contentDescription = "Nombre del musico: ${musician.name}"
                    }
                )
                if (!musician.birthDate.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Nacimiento: ${musician.birthDate.take(10)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMusicianSheet(
    sheetState: SheetState,
    bandId: Int,
    currentMusicianIds: Set<Int>,
    viewModel: BandViewModel,
    onDismiss: () -> Unit
) {
    val allMusicians    by viewModel.allMusicians.collectAsState()
    val musiciansLoading by viewModel.musiciansLoading.collectAsState()
    val addLoading      by viewModel.addMusicianLoading.collectAsState()
    val addError        by viewModel.addMusicianError.collectAsState()

    val available = allMusicians.filter { it.id !in currentMusicianIds }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                BottomSheetDefaults.DragHandle()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(top = 12.dp)
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                HorizontalDivider(
                    modifier = Modifier.width(32.dp),
                    thickness = 2.dp,
                    color = VinilosPrimary
                )

                Text(
                    text = "Asociar músico",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1C1C1C)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (addError != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = addError!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            when {
                musiciansLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = VinilosPrimary)
                    }
                }

                available.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Todos los músicos ya están asociados a esta banda.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = SecondaryText
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = rememberLazyListState(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(available, key = { it.id }) { musician ->
                            MusicianPickerRow(
                                musician = musician,
                                isLoading = addLoading,
                                onClick = {
                                    viewModel.addMusicianToBand(
                                        bandId,
                                        musician.id
                                    ) {
                                        onDismiss()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MusicianPickerRow(
    musician: Performer,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(musician.image)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = VinilosPrimary,
                        strokeWidth = 2.dp
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize().background(PlaceholderBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = VinilosPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = musician.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color(0xFF1C1C1C)
            )
            if (!musician.birthDate.isNullOrBlank()) {
                Text(
                    text = musician.birthDate!!.take(10),
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = onClick,
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = VinilosPrimary),
            shape = RoundedCornerShape(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            modifier = Modifier.semantics {
                contentDescription = "Agregar músico ${musician.name}"
            }
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Agregar",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    HorizontalDivider(color = Color(0xFFF1ECE8))
}
