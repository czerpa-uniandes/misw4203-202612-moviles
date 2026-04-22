package com.team4.vinilosapp

import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Comment
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.models.Track

object TestData {
    fun album(
        id: String = "1",
        name: String = "Poeta del pueblo"
    ) = Album(
        id = id,
        name = name,
        cover = "https://example.com/cover.jpg",
        releaseDate = "2020-01-01T00:00:00Z",
        description = "Descripción de prueba",
        genre = "Rock",
        recordLabel = "Sony Music",
        tracks = listOf(
            Track(
                id = 1,
                name = "Canción 1",
                duration = "03:45"
            )
        ),
        performers = listOf(
            Performer(
                id = 1,
                name = "Artista prueba",
                image = "",
                description = "",
                birthDate = ""
            )
        ),
        comments = listOf(
            Comment(
                id = 1,
                description = "Buen álbum",
                rating = 5
            )
        )
    )
}