package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Performer

class BandRepository(private val serviceAdapter: VinilosServiceAdapter) {
    suspend fun getBands(): Result<List<Performer>> {
        return try {
            Result.success(serviceAdapter.getBands())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMusicians(): Result<List<Performer>> {
        return try {
            Result.success(serviceAdapter.getMusicians())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBandDetail(bandId: Int): Result<BandDetail> {
        return try {
            Result.success(serviceAdapter.getBandDetail(bandId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMusicianToBand(bandId: Int, musicianId: Int): Result<Unit> {
        return try {
            serviceAdapter.addMusicianToBand(bandId, musicianId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
