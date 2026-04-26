package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail

class CollectorRepository(private val serviceAdapter: VinilosServiceAdapter) {
    suspend fun getCollectors(): Result<List<Collector>> {
        return try {
            Result.success(serviceAdapter.getCollectors())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCollectorById(collectorId: Int): Result<CollectorDetail> {
        return try {
            val collector = serviceAdapter.getCollectorDetail(collectorId);
            Result.success(collector);
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
