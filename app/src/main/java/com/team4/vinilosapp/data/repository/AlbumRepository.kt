package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum

class AlbumRepository(
    private val serviceAdapter: VinilosServiceAdapter
) {
    suspend fun getAlbums(): Result<List<Album>> {
        return try {
            val albums = serviceAdapter.getAlbums()
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

            serviceAdapter.createAlbum(body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAlbumDetail(albumId: Int): Result<Album> {
        return try {
            val album = serviceAdapter.getAlbumDetail(albumId)
            Result.success(album)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTrack(albumId: Int, track: AddTrack): Result<Unit> {
        return try {
            val body = AddTrack(
                name = track.name,
                duration = track.duration
            )

            serviceAdapter.addTrack(albumId, body)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}