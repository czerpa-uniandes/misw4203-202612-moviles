package com.team4.vinilosapp.data.adapters

import com.team4.vinilosapp.data.models.AddAlbumToCollectorRequest
import com.team4.vinilosapp.data.models.AddAlbumToCollectorResponse
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.AlbumCommentRequest
import com.team4.vinilosapp.data.models.AlbumCommentResponse
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Comment
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum

interface VinilosServiceAdapter {
    suspend fun getAlbums(): List<Album>
    suspend fun getAlbumDetail(albumId: Int): Album
    suspend fun createAlbum(album: NewAlbum)
    suspend fun addTrack(albumId: Int, track: AddTrack)
    suspend fun getMusicians(): List<Performer>
    suspend fun getArtistDetail(artistId: Int): ArtistDetail
    suspend fun getBands(): List<Performer>
    suspend fun getBandDetail(bandId: Int): BandDetail
    suspend fun addMusicianToBand(bandId: Int, musicianId: Int)
    suspend fun getCollectors(): List<Collector>
    suspend fun getCollectorDetail(collectorId: Int): CollectorDetail
    suspend fun addComment(albumId: String, comment: AlbumCommentRequest): AlbumCommentResponse
    suspend fun addAlbumToCollector(albumId: String, collectorId: String, albumToCollector: AddAlbumToCollectorRequest): AddAlbumToCollectorResponse
}