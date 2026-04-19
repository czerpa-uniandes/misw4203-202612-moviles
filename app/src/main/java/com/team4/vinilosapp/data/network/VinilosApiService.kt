package com.team4.vinilosapp.data.network

import com.team4.vinilosapp.data.model.AlbumDetailDto
import retrofit2.http.GET
import retrofit2.http.Path

interface VinilosApiService {
    @GET("albums/{id}/")
    suspend fun getAlbumDetail(
        @Path("id") albumId: Int
    ): AlbumDetailDto
}