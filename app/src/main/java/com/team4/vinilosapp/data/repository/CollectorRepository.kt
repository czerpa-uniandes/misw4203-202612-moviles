package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Collector

class CollectorRepository(private val serviceAdapter: VinilosServiceAdapter) {
    suspend fun getCollectors(): Result<List<Collector>> {
        return try {
            Result.success(serviceAdapter.getCollectors())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
