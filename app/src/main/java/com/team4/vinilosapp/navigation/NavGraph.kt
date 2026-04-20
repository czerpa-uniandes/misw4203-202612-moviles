package com.team4.vinilosapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.team4.vinilosapp.ui.screens.albums.AddTrackAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumsScreen
import com.team4.vinilosapp.ui.screens.albums.CreateAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumDetailScreen
import com.team4.vinilosapp.ui.screens.home.HomeScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Collectors : Screen("collectors")
    object Albums : Screen("albums")
    object CreateAlbum : Screen("create_album")
    object AlbumDetail : Screen("albumDetail/{albumId}/{sectionTitle}") {
        fun createRoute(albumId: Int, sectionTitle: String) =
            "albumDetail/$albumId/$sectionTitle"
    }
    object AddTrackAlbum : Screen("add_track_album/{albumId}") {
        fun createRoute(albumId: String) = "add_track_album/$albumId"
    }
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Collectors.route) {
            CollectorsScreen(navController)
        }

        composable(Screen.Albums.route) {
            AlbumsScreen(navController)
        }

        composable(Screen.CreateAlbum.route) {
            CreateAlbumScreen(navController)
        }

        composable(Screen.AlbumDetail.route) { backStackEntry ->

            val albumId =
                backStackEntry.arguments?.getString("albumId")?.toIntOrNull() ?: 0

            val sectionTitle =
                backStackEntry.arguments?.getString("sectionTitle") ?: "Álbumes"

            AlbumDetailScreen(
                navController = navController,
                albumId = albumId,
                sectionTitle = sectionTitle
            )
        }

        composable(Screen.AddTrackAlbum.route) { backStackEntry ->

            val albumId =
                backStackEntry.arguments?.getString("albumId")?.toIntOrNull() ?: 0

            AddTrackAlbumScreen(
                navController = navController,
                albumId = albumId
            )
        }
    }
}