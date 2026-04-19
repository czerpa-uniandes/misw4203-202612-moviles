package com.team4.vinilosapp.ui.screens.albums

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.navigation.Screen
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.models.AlbumFilter
import com.team4.vinilosapp.ui.screens.albums.components.*
import com.team4.vinilosapp.ui.viewmodels.AlbumViewModel

@Composable
fun AlbumsScreen(navController: NavController) {
    val viewModel: AlbumViewModel = viewModel()
    var showFilter by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNav(navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_album") },
                containerColor = Color(0xFFB4532A),
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                },
                text = {
                    Text("Añadir álbum", color = Color.White)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 100.dp,
                    bottom = 80.dp
                )
            ) {
                item { HeaderSection(
                    onFilterClick = {
                        showFilter = true
                    }
                ) }
                item { AlbumsList(navController) }
            }

            if (showFilter) {
                FilterBottomSheet(
                    onDismiss = { showFilter = false },
                    onApply = { filter: AlbumFilter ->
                        viewModel.updateFilter(filter)
                        showFilter = false
                    }
                )
            }

            ToolbarSection(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
            )
        }
    }
}