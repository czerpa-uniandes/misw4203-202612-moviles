package com.team4.vinilosapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.team4.vinilosapp.ui.screens.albums.AlbumsScreen
import com.team4.vinilosapp.ui.screens.home.HomeScreen
import com.team4.vinilosapp.ui.screens.collectors.CollectorsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Collectors : Screen("collectors")
    object Albums : Screen("albums")
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
    }
}