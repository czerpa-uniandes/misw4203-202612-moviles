package com.team4.vinilosapp.data.models

data class Prize(
    val id: Int,
    val name: String,
    val description: String,
    val organization: String,
    val performerPrizes: List<PerformerPrize> = emptyList()
)