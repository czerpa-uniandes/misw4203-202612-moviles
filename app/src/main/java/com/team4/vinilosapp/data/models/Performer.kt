package com.team4.vinilosapp.data.models

import com.squareup.moshi.Json

data class Performer(
    val id: Int,
    val name: String,
    val image: String?,
    val description: String?,
    @Json(name = "birthDate")
    val birthDate: String?
)