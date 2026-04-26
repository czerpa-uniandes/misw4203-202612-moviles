package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider

class ArtistRepository(private val serviceAdapter: VinilosServiceAdapter) {
    suspend fun getArtists(): Result<List<Performer>> {
        return try {
            val musicians = serviceAdapter.getMusicians()
            Result.success(musicians)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
