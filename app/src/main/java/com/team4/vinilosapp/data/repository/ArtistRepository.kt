package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.Performer

class ArtistRepository(private val serviceAdapter: VinilosServiceAdapter) {
    suspend fun getArtists(): Result<List<Performer>> {
        return try {
            val musicians = serviceAdapter.getMusicians()
            Result.success(musicians)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getArtistDetail(artistId: Int): Result<ArtistDetail> {
        return try {
            Result.success(serviceAdapter.getArtistDetail(artistId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
