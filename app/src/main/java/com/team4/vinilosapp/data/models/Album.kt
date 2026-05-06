package com.team4.vinilosapp.data.models

data class Album(
    val id: String,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String,
    val tracks: List<Track> = emptyList(),
    val performers: List<Performer> = emptyList(),
    val comments: List<Comment> = emptyList(),
)

data class AlbumReference(
    val id: String,
    val name: String,
    val cover: String,
    val releaseDate: String,
    val description: String,
    val genre: String,
    val recordLabel: String
)

data class AlbumCommentRequest(
    val description: String,
    val rating: Int,
    val collector: CollectorIdRequest
)

data class AlbumCommentResponse(
    val description: String,
    val rating: Int,
    val collector: Collector,
    val album: AlbumReference,
    val id: Int
)

data class CollectorIdRequest(
    val id: Int
)

data class AddAlbumToCollectorRequest(
    val price: Int,
    val status: String
)

data class AddAlbumToCollectorResponse(
    val id: Int,
    val price: Int,
    val status: String,
    val album: AlbumReference,
    val collector: Collector,
)