package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.model.AlbumDetailDto
import com.team4.vinilosapp.data.network.RetrofitProvider

class AlbumRepository {
    suspend fun getAlbumDetail(albumId: Int): AlbumDetailDto {
        return RetrofitProvider.api.getAlbumDetail(albumId)
    }
}