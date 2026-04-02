package com.team4.vinilosapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.screens.home.components.*

@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        bottomBar = { BottomNav(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            item { HeaderSection() }

            item { FeaturedArtistCard() }

            item { ArtistList() }
        }
    }
}