package com.team4.vinilosapp.data.model

data class AlbumDetailDto(
    val id: Int,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<TrackDto>,
    val performers: List<PerformerDto>,
    val comments: List<CommentDto>
)

data class TrackDto(
    val id: Int,
    val name: String,
    val duration: String
)

data class PerformerDto(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val birthDate: String
)

data class CommentDto(
    val id: Int,
    val description: String,
    val rating: Int
)