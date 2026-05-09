package com.team4.vinilosapp.data.network

import com.team4.vinilosapp.data.models.AddAlbumToCollectorRequest
import com.team4.vinilosapp.data.models.AddAlbumToCollectorResponse
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.AlbumCommentRequest
import com.team4.vinilosapp.data.models.AlbumCommentResponse
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddPrize
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VinilosApiService {
    @GET("albums/{id}/")
    suspend fun getAlbumDetail(
        @Path("id") albumId: Int
    ): Album

    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @POST("albums")
    suspend fun createAlbum(
        @Body album: NewAlbum
    ): Response<Unit>

    @POST("albums/{id}/tracks")
    suspend fun addTrack(
        @Path("id") albumId: Int,
        @Body track: AddTrack
    ): Response<Unit>

    @GET("musicians")
    suspend fun getMusicians(): List<Performer>

    @GET("musicians/{id}")
    suspend fun getArtistDetail(
        @Path("id") artistId: Int
    ): ArtistDetail

    @GET("bands")
    suspend fun getBands(): List<Performer>

    @GET("bands/{id}")
    suspend fun getBandDetail(
        @Path("id") bandId: Int
    ): BandDetail

    @POST("bands/{bandId}/musicians/{musicianId}")
    suspend fun addMusicianToBand(
        @Path("bandId") bandId: Int,
        @Path("musicianId") musicianId: Int
    ): Response<Unit>

    @POST("musicians/{musicianId}/albums/{albumId}")
    suspend fun addAlbumToMusician(
        @Path("musicianId") musicianId: Int,
        @Path("albumId") albumId: Int
    ): Response<Unit>

    @GET("collectors")
    suspend fun getCollectors(): List<Collector>

    @GET("collectors/{id}/")
    suspend fun getCollectorDetail(
        @Path("id") collectorId: Int
    ): CollectorDetail

    @POST("albums/{albumId}/comments")
    suspend fun addComment(
        @Path("albumId") albumId: String,
        @Body comment: AlbumCommentRequest
    ): AlbumCommentResponse

    @POST("collectors/{collectorId}/albums/{albumId}")
    suspend fun addAlbumToCollector(
        @Path("collectorId") collectorId: String,
        @Path("albumId") albumId: String,
        @Body request: AddAlbumToCollectorRequest
    ): AddAlbumToCollectorResponse

    @POST("prizes")
    suspend fun addPrize(
        @Body prize: AddPrize
    ): Response<Unit>
}