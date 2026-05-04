package com.team4.vinilosapp.ui.screens.prizes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.team4.vinilosapp.ui.components.BottomNav
import com.team4.vinilosapp.ui.screens.prizes.components.AddPrizeTopBar
import com.team4.vinilosapp.ui.viewmodels.PrizeViewModel

private val VinilosPrimary = Color(0xFFB44A1F)

@Composable
fun AddPrizeScreen(navController: NavController) {
    val viewModel: PrizeViewModel = viewModel()

    val loading by viewModel.createLoading.collectAsState()
    val success by viewModel.createSuccess.collectAsState()
    val error by viewModel.createError.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }

    LaunchedEffect(success) {
        if (success) {
            viewModel.resetCreateState()
            navController.popBackStack()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = { AddPrizeTopBar(navController) },
        bottomBar = { BottomNav(navController) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PrizeTextField(
                label = "Nombre del premio",
                placeholder = "Ej. Grammy",
                value = name,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("prize_name_input"),
                onChange = { name = it }
            )

            PrizeTextField(
                label = "Descripción",
                placeholder = "Ej. Premio otorgado a la excelencia musical",
                value = description,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("prize_description_input"),
                minLines = 3,
                onChange = { description = it }
            )

            PrizeTextField(
                label = "Organización",
                placeholder = "Ej. The Recording Academy",
                value = organization,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .testTag("prize_organization_input"),
                onChange = { organization = it }
            )

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            OutlinedButton(
                onClick = {
                    viewModel.createPrize(
                        name = name,
                        description = description,
                        organization = organization
                    )
                },
                enabled = !loading &&
                        name.isNotBlank() &&
                        description.isNotBlank() &&
                        organization.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 8.dp)
                    .testTag("add_prize_button"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = VinilosPrimary,
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.DarkGray
                ),
                border = BorderStroke(
                    1.dp,
                    if (name.isNotBlank() && description.isNotBlank() && organization.isNotBlank())
                        VinilosPrimary
                    else
                        Color.Gray
                )
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar premio")
                }
            }
        }
    }
}

@Composable
fun PrizeTextField(
    label: String,
    placeholder: String,
    value: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    onChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = VinilosPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            modifier = modifier,
            minLines = minLines,
            shape = RoundedCornerShape(24.dp),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black,

                focusedBorderColor = VinilosPrimary,
                unfocusedBorderColor = Color.Gray,

                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,

                cursorColor = VinilosPrimary,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}