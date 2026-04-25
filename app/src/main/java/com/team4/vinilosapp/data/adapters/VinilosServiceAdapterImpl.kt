package com.team4.vinilosapp.data.adapters

import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.VinilosApiService
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum

class VinilosServiceAdapterImpl(
    private val api: VinilosApiService
) : VinilosServiceAdapter {

    override suspend fun getAlbums(): List<Album> {
        return api.getAlbums()
    }

    override suspend fun getAlbumDetail(albumId: Int): Album {
        return api.getAlbumDetail(albumId)
    }

    override suspend fun createAlbum(album: NewAlbum) {
        val response = api.createAlbum(album)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun addTrack(albumId: Int, track: AddTrack) {
        val response = api.addTrack(albumId, track)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }
    }

    override suspend fun getMusicians(): List<Performer> {
        return api.getMusicians()
    }

    override suspend fun getBands(): List<Performer> {
        return api.getBands()
    }

    override suspend fun getCollectors(): List<Collector> {
        return api.getCollectors()
    }

    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail {
        return api.getCollectorDetail(collectorId);
    }
}