package com.team4.vinilosapp.ui.screens.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.team4.vinilosapp.navigation.Screen
import com.team4.vinilosapp.ui.components.BottomNav

@Composable
fun AlbumsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNav(navController = navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Albums Screen")
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text("Volver a Home")
            }

            Button(
                onClick = {
                    navController.navigate(
                        Screen.AlbumDetail.createRoute(
                            albumId = 100,
                            sectionTitle = "Álbumes"
                        )
                    )
                }
            ) {
                Text("Ver detalle álbum")
            }
        }
    }
}