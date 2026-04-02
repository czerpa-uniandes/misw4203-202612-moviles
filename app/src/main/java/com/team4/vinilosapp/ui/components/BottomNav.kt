package com.team4.vinilosapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.graphics.Color


@Composable
fun BottomNav(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("home") },
            label = { Text("Artistas") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("collectors") },
            label = { Text("Coleccionistas") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("albums") },
            label = { Text("Albumes") },
            icon = { Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null, tint = Color.Black)
            }
        )
    }
}