package com.team4.vinilosapp.data.models

import com.squareup.moshi.Json

data class ArtistDetail(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String?,
    @Json(name = "birthDate")
    val birthDate: String?,
    val albums: List<AlbumReference> = emptyList(),
    val bands: List<Performer> = emptyList(),
    val performerPrizes: List<PerformerPrize> = emptyList()
)

data class PerformerPrize(
    val id: Int,
    val premiationDate: String? = null,
    val prize: Prize? = null
)

data class Prize(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val organization: String? = null
)
