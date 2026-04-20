package com.team4.vinilosapp.data.network

import com.team4.vinilosapp.data.models.Album
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
}