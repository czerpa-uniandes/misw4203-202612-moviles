package com.team4.vinilosapp.data.models

import com.squareup.moshi.Json

data class BandDetail(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String?,
    @Json(name = "creationDate")
    val creationDate: String?,
    val musicians: List<Performer> = emptyList()
)
