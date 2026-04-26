package com.team4.vinilosapp.data.adapters

import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum

interface VinilosServiceAdapter {
    suspend fun getAlbums(): List<Album>
    suspend fun getAlbumDetail(albumId: Int): Album
    suspend fun createAlbum(album: NewAlbum)
    suspend fun addTrack(albumId: Int, track: AddTrack)
    suspend fun getMusicians(): List<Performer>
    suspend fun getBands(): List<Performer>
    suspend fun getCollectors(): List<Collector>
    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail
}