package com.team4.vinilosapp

import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Comment
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.models.Track

object TestData {
    fun performer(
        id: Int = 1,
        name: String = "Artista de prueba",
        image: String = "https://example.com/image.jpg",
        description: String = "Descripción del artista",
        birthDate: String? = "1970-01-01T00:00:00Z"
    ) = Performer(
        id = id,
        name = name,
        image = image,
        description = description,
        birthDate = birthDate
    )

    fun collector(
        id: Int = 1,
        name: String = "Coleccionista de prueba",
        telephone: String = "3001234567",
        email: String = "coleccionista@example.com",
        image: String? = "https://example.com/collector.jpg"
    ) = Collector(id = id, name = name, telephone = telephone, email = email, image = image)

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

    fun collectorDetail(
        id: Int = 100,
        name: String = "Manolo Bellon",
        telephone: String = "3502457896",
        email: String = "manollo@caracol.com.co"
    ) = CollectorDetail(
        id = id,
        name = name,
        telephone = telephone,
        email = email,
        comments = emptyList(),
        favoritePerformers = emptyList(),
        collectorAlbums = emptyList()

    )
}