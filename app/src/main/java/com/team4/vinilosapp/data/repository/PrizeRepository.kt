package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.models.Prize
import com.team4.vinilosapp.ui.models.AddPrize
import com.team4.vinilosapp.ui.models.AddPrizeArtist
import com.team4.vinilosapp.ui.models.AddTrack

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

    suspend fun getPrizes(): Result<List<Prize>> {
        return try {
            Result.success(serviceAdapter.getPrizes())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun associatePrizeArtist(prizeId: Int, artistId: Int, premiationDate: String): Result<Unit> {
        return try {
            val body = AddPrizeArtist(
                premiationDate = premiationDate
            )

            serviceAdapter.associatePrizeArtist(prizeId, artistId, body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}