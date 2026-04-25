package com.team4.vinilosapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.team4.vinilosapp.ui.screens.albums.AddTrackAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumsScreen
import com.team4.vinilosapp.ui.screens.albums.CreateAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumDetailScreen
import com.team4.vinilosapp.ui.screens.artists.ArtistsScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorDetailScreen
import com.team4.vinilosapp.ui.screens.home.HomeScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Artists : Screen("artists")
    object Collectors : Screen("collectors")

    object CollectorDetail : Screen("collector_detail/{collectorId}") {
        fun createRoute(albumId: Int) = "collector_detail/$albumId"
    }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Artists.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Artists.route) {
            ArtistsScreen(navController)
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

        composable(Screen.CollectorDetail.route) { backStackEntry ->

            val collectorId =
                backStackEntry.arguments?.getString("collectorId")?.toIntOrNull() ?: 0

            CollectorDetailScreen(
                navController = navController,
                collectorId = collectorId
            )
        }
    }
}