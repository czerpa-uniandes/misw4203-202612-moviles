package com.team4.vinilosapp.ui.screens.prizes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav

@Composable
fun PrizesScreen(navController: NavController) {

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNav(navController) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("create_prize") },
                containerColor = Color(0xFFB4532A),
                icon = {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                },
                text = {
                    Text("Añadir Premio", color = Color.White)
                }
            )
        }
    ){ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {


        }
    }
}