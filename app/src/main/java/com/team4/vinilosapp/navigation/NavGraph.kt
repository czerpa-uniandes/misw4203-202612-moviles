package com.team4.vinilosapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.team4.vinilosapp.ui.screens.albums.AddAlbumToCollectorScreen
import com.team4.vinilosapp.ui.screens.albums.AddTrackAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumsScreen
import com.team4.vinilosapp.ui.screens.albums.CreateAlbumScreen
import com.team4.vinilosapp.ui.screens.albums.AlbumDetailScreen
import com.team4.vinilosapp.ui.screens.albums.CommentAlbumScreen
import com.team4.vinilosapp.ui.screens.artists.ArtistDetailScreen
import com.team4.vinilosapp.ui.screens.artists.ArtistsScreen
import com.team4.vinilosapp.ui.screens.bands.BandDetailScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorDetailScreen
import com.team4.vinilosapp.ui.screens.home.HomeScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorsScreen
import com.team4.vinilosapp.ui.screens.prizes.AddPrizeScreen
import com.team4.vinilosapp.ui.screens.prizes.PrizesScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Artists : Screen("artists")
    object ArtistDetail : Screen("artist_detail/{artistId}") {
        fun createRoute(artistId: Int) = "artist_detail/$artistId"
    }
    object Collectors : Screen("collectors")

    object CollectorDetail : Screen("collector_detail/{collectorId}") {
        fun createRoute(albumId: Int) = "collector_detail/$albumId"
    }
    object BandDetail : Screen("band_detail/{bandId}") {
        fun createRoute(bandId: Int) = "band_detail/$bandId"
    }
    object Albums : Screen("albums")
    object CreateAlbum : Screen("create_album")
    object AlbumDetail : Screen("albumDetail/{albumId}/{sectionTitle}") {
        fun createRoute(albumId: Int, sectionTitle: String) =
            "albumDetail/$albumId/$sectionTitle"
    }
    object CommentAlbum : Screen("comment_album/{albumId}") {
        fun createRoute(albumId: String): String {
            return "comment_album/$albumId"
        }
    }
    object AddAlbumToCollector : Screen("add_album_collector/{albumId}") {
        fun createRoute(albumId: String): String {
            return "add_album_collector/$albumId"
        }
    }
    object AddTrackAlbum : Screen("add_track_album/{albumId}") {
        fun createRoute(albumId: String) = "add_track_album/$albumId"
    }

    object Prizes : Screen("prizes")
    object AddPrize : Screen("create_prize")
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

        composable(Screen.ArtistDetail.route) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId")?.toIntOrNull() ?: 0
            ArtistDetailScreen(navController = navController, artistId = artistId)
        }

        composable(Screen.BandDetail.route) { backStackEntry ->
            val bandId = backStackEntry.arguments?.getString("bandId")?.toIntOrNull() ?: 0
            BandDetailScreen(navController = navController, bandId = bandId)
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

        composable(
            route = Screen.CommentAlbum.route,
            arguments = listOf(
                navArgument("albumId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""

            CommentAlbumScreen(
                navController = navController,
                albumId = albumId
            )
        }

        composable(
            route = Screen.AddAlbumToCollector.route,
            arguments = listOf(
                navArgument("albumId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: "0"

            AddAlbumToCollectorScreen(
                navController = navController,
                albumId = albumId
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

        composable(Screen.Prizes.route) {
            PrizesScreen(navController)
        }


        composable(Screen.AddPrize.route) {
            AddPrizeScreen(navController)
        }
    }
}