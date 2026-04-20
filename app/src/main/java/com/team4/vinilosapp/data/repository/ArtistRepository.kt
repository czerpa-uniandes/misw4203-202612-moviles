package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider

class ArtistRepository {
    suspend fun getArtists(): Result<List<Performer>> {
        return try {
            val musicians = RetrofitProvider.api.getMusicians()
            val bands = RetrofitProvider.api.getBands()
            Result.success(musicians + bands)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
