package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.ui.models.AddPrize

class PrizeRepository(
    private val serviceAdapter: VinilosServiceAdapter
) {
    suspend fun createPrize(prize: AddPrize): Result<Unit> {
        return try {
            val body = AddPrize(
                name = prize.name,
                description = prize.description,
                organization = prize.organization
            )

            serviceAdapter.addPrize(body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}