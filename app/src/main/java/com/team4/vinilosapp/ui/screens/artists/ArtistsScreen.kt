package com.team4.vinilosapp.ui.screens.artists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.screens.artists.components.ArtistsList
import com.team4.vinilosapp.ui.screens.bands.components.BandsList
import com.team4.vinilosapp.ui.viewmodels.ArtistViewModel
import com.team4.vinilosapp.ui.viewmodels.BandViewModel
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics

@Composable
fun ArtistsScreen(navController: NavController) {
    val artistViewModel: ArtistViewModel = viewModel()
    val bandViewModel: BandViewModel = viewModel()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var query by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(selectedTab) {
        query = ""
        if (selectedTab == 0) artistViewModel.search("") else bandViewModel.search("")
    }

    val tabs = listOf("Artistas", "Bandas")

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = tabs[selectedTab],
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = Color(0xFFB4532A),
                    modifier = Modifier.semantics {
                        heading()
                        contentDescription = "Título de sección ${tabs[selectedTab]}"
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = Color(0xFFB4532A)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            modifier = Modifier.semantics {
                                contentDescription = "Pestaña $title"
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        if (selectedTab == 0) artistViewModel.search(it) else bandViewModel.search(it)
                    },
                    placeholder = { Text("Buscar ${tabs[selectedTab].lowercase()}...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFB4532A),
                        unfocusedBorderColor = Color.LightGray
                    )
                )
            }

            if (selectedTab == 0) {
                ArtistsList(
                    onArtistClick = { artistId ->
                        navController.navigate("artist_detail/$artistId")
                    },
                    modifier = Modifier.weight(1f)
                )
            } else {
                BandsList(
                    onBandClick = { bandId ->
                        navController.navigate("band_detail/$bandId")
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
