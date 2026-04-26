package com.team4.vinilosapp.data.models

data class CollectorDetail (
    val id: Int,
    val name: String,
    val telephone: String,
    val email: String,
    val comments: List<Comment> = emptyList(),
    val favoritePerformers: List<Performer> = emptyList(),
    val collectorAlbums: List<CollectorAlbum> = emptyList()
)

data class CollectorAlbum(
    val id: Int,
    val price: Int,
    val status: String
)