package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.SelectableDates

@Composable
fun ReleaseDateField(
    value: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "FECHA DE LANZAMIENTO",
            fontSize = 12.sp,
            color = Color(0xFFB4532A),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                enabled = false,
                placeholder = { Text("Ej: 1973-01-01") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color.LightGray,
                    disabledTextColor = Color.Black,
                    disabledPlaceholderColor = Color.Gray,
                    disabledTrailingIconColor = Color(0xFFB4532A),
                    focusedBorderColor = Color(0xFFB4532A),
                    focusedLabelColor = Color(0xFFB4532A),
                    cursorColor = Color(0xFFB4532A)
                )
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {

                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val today = System.currentTimeMillis()
                    return utcTimeMillis <= today
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            colors = DatePickerDefaults.colors(
                containerColor = Color.White,
                selectedDayContainerColor = Color(0xFFB4532A),
                selectedDayContentColor = Color.White,
            ),
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            onDateSelected(formatter.format(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = Color(0xFFB4532A) )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color(0xFF8F8F8F))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = Color(0xFFB4532A),
                    selectedDayContentColor = Color.White,
                    todayDateBorderColor = Color(0xFFB4532A),
                    todayContentColor = Color(0xFFB4532A)
                )
            )
        }
    }
}