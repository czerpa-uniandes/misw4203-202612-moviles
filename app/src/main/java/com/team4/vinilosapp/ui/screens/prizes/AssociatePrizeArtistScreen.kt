package com.team4.vinilosapp.ui.screens.prizes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.screens.prizes.components.AssociatePrizeArtistTopBar
import com.team4.vinilosapp.ui.viewmodels.PrizeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssociatePrizeArtistScreen(
    navController: NavController,
    artistId: Int,
    viewModel: PrizeViewModel = viewModel()
) {
    val prizes by viewModel.prizes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedPrizeId by remember { mutableStateOf<Int?>(null) }
    var premiationDate by remember { mutableStateOf("") }
    val isDateValid = remember(premiationDate) {
        Regex("""^\d{4}-\d{2}-\d{2}$""").matches(premiationDate)
    }
    var expanded by remember { mutableStateOf(false) }

    val associateLoading by viewModel.associateLoading.collectAsState()
    val associateSuccess by viewModel.associateSuccess.collectAsState()
    val associateError by viewModel.associateError.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.fetchPrizes()
    }

    LaunchedEffect(associateSuccess) {
        if (associateSuccess) {
            viewModel.resetAssociateState()
            navController.navigate("artist_detail/$artistId") {
                popUpTo("artist_detail/$artistId") {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { AssociatePrizeArtistTopBar(navController) },
        bottomBar = { BottomNav(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFFB4532A))
            } else {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = prizes.find { it.id == selectedPrizeId }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecciona un premio") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("prize_selected"),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        prizes.forEach { prize ->
                            DropdownMenuItem(
                                text = { Text(prize.name) },
                                onClick = {
                                    selectedPrizeId = prize.id
                                    expanded = false
                                },
                                modifier = Modifier.testTag("prize_option_${prize.id}")
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = premiationDate,
                    onValueChange = { premiationDate = it },
                    label = { Text("Fecha de premiación") },
                    placeholder = { Text("YYYY-MM-DD") },
                    isError = premiationDate.isNotBlank() && !isDateValid,
                    supportingText = {
                        if (premiationDate.isNotBlank() && !isDateValid) {
                            Text("Usa el formato YYYY-MM-DD")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                        .testTag("prize_premiation_date_input"),
                )

                Button(
                    onClick = {
                        selectedPrizeId?.let { prizeId ->
                            viewModel.associatePrizeArtist(
                                prizeId = prizeId,
                                artistId = artistId,
                                premiationDate = premiationDate
                            )
                        }
                    },
                    enabled = selectedPrizeId != null &&
                            premiationDate.isNotBlank() &&
                            isDateValid &&
                            !associateLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB4532A),
                        contentColor = Color.White,

                        disabledContainerColor = Color(0xFFE0E0E0),
                        disabledContentColor = Color(0xFF5F5E5C)
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .testTag("prize_associate_button")

                ) {
                    if (associateLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                associateError?.let {
                    Text(
                        text = it,
                        color = Color.Red
                    )
                }
            }
        }
    }
}