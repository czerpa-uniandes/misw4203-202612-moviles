package com.team4.vinilosapp.ui.screens.albums.components

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun CoverUploader(
    imageUri: Uri?,
    onClick: () -> Unit
) {
    val borderColor = Color(0xFFDDCFC8)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp)
            .clickable { onClick() }
            .drawBehind {
                val strokeWidth = 3.dp.toPx()
                drawRoundRect(
                    color = borderColor,
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(40f, 40f),
                    style = Stroke(
                        width = strokeWidth,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(20f, 10f),
                            0f
                        )
                    )
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Carátula seleccionada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Subir carátula",
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "JPG O PNG · MAX 5MB",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}