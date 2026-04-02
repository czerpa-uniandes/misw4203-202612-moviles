package com.team4.vinilosapp.ui.screens.collectors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav

@Composable
fun CollectorsScreen(navController: NavController) {

    Scaffold(
        bottomBar = {
            BottomNav(navController)
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding)) {
            Text("Collectors Screen")

            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text("Volver a Home")
            }
        }
    }
}