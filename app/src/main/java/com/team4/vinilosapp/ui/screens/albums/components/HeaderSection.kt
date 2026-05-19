package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HeaderSection(
    onFilterClick: () -> Unit
) {
    Column(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 50.dp)
    ) {
        Text(
            text = "CATÁLOGO CURADO",
            color = Color(0xFFA94C26),
            modifier = Modifier.semantics {
                contentDescription = "Catalogo curado"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "La esencia del",
            modifier = Modifier.semantics {
                contentDescription = "La esencia del"
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "sonido analógico",
            modifier = Modifier.semantics {
                contentDescription = "sonido analógico"
            },
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFA94C26)
        )

        Box(
            modifier = Modifier
                .testTag("filter_button")
                .padding(16.dp)
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .semantics {
                    contentDescription = "Abrir filtros"
                }
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filtros"
            )
        }
    }
}