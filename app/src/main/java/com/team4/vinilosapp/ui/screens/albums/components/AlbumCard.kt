package com.team4.vinilosapp.ui.screens.albums.components

import android.R.attr.contentDescription
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.team4.vinilosapp.navigation.Screen

@Composable
fun AlbumCard(
    id: String,
    title: String,
    artist: String,
    imageUrl: String,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .clearAndSetSemantics { }
                .fillMaxWidth()
        ) {
            Column {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Imagen del album $title",
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .height(220.dp)
                        .background(Color.Black)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                )

                Column(
                    modifier = Modifier
                        .padding(12.dp)
                        .semantics {
                            this.contentDescription = "Ver más sobre el álbum $title de $artist"
                        }
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Text(
                        text = artist,
                        color = Color(0xFF767676),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate(
                    Screen.AlbumDetail.createRoute(
                        albumId = id.toInt(),
                        sectionTitle = "Álbumes"
                    )
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF2F2F2),
                contentColor = Color(0xFFA94C26)
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .semantics {
                    this.contentDescription = "Ver más sobre el álbum $title"
                }
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .offset(y = 8.dp)
                .testTag("album_list_item_$id")
        ) {
            Text(
                text = "Ver más",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}