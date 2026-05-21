package com.team4.vinilosapp.ui.screens.collectors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.screens.collectors.components.CollectorsList
import com.team4.vinilosapp.ui.viewmodels.CollectorViewModel

private val Primary = Color(0xFF9D3E1D)
private val TextPrimary = Color(0xFF1D1B20)
private val SecondaryText = Color(0xFF5F5E5C)

@Composable
fun CollectorsScreen(navController: NavController) {
    val viewModel: CollectorViewModel = viewModel()
    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Coleccionistas",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.search(it)
                    },
                    placeholder = { Text("Buscar coleccionista...", color = SecondaryText) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = SecondaryText)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1D1B20),
                        unfocusedTextColor = Color(0xFF1D1B20),
                        cursorColor = Primary,

                        focusedBorderColor = Primary,
                        unfocusedBorderColor = Primary,

                        focusedPlaceholderColor = SecondaryText,
                        unfocusedPlaceholderColor = SecondaryText,

                        focusedLeadingIconColor = SecondaryText,
                        unfocusedLeadingIconColor = SecondaryText,

                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
            }

            CollectorsList(
                navController = navController,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
