package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.ui.models.NewAlbum
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.ui.models.AddTrack

class AlbumRepository() {
    suspend fun getAlbums(): Result<List<Album>> {
        return try {
            val albums = RetrofitProvider.api.getAlbums()
            Result.success(albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createAlbum(album: NewAlbum): Result<Unit> {
        return try {
            val body = NewAlbum(
                title = album.title,
                genre = album.genre,
                cover = album.cover,
                description = album.description,
                releaseDate = album.releaseDate,
                recordLabel = album.recordLabel
            )

            val response = RetrofitProvider.api.createAlbum(body)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumDetail(albumId: Int): Album {
        return RetrofitProvider.api.getAlbumDetail(albumId)
    }

    suspend fun addTrack(albumId: Int, track: AddTrack): Result<Unit> {
        return try {
            val body = AddTrack(
                name = track.name,
                duration = track.duration,
            )

            val response = RetrofitProvider.api.addTrack(albumId, body)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}