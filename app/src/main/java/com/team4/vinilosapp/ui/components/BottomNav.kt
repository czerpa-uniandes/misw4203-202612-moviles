package com.team4.vinilosapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag


@Composable
fun BottomNav(navController: NavController) {

    NavigationBar {

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("artists") },
            label = { Text("Artistas") },
            icon = { Icon(Icons.Default.MusicNote, contentDescription = null) }
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
            },
            modifier = Modifier.testTag("bottom_nav_albums")
        )
    }
}