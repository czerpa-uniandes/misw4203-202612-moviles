package com.team4.vinilosapp.data.models

import com.google.gson.annotations.SerializedName

data class Performer(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    @SerializedName(value = "birthDate", alternate = ["creationDate"])
    val birthDate: String?
)