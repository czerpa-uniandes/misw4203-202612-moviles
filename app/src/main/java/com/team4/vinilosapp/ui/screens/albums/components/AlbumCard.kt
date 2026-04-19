package com.team4.vinilosapp.ui.screens.albums.components

import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AlbumCard(
    title: String,
    artist: String,
    imageUrl: String
) {

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .height(220.dp)
                        .background(Color.Black)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                )

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = artist,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF2F2F2),
                contentColor = Color(0xFFB4532A)
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = 8.dp)
        ) {
            Text(
                text = "Ver más",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}