package com.team4.vinilosapp.data.repository

import android.content.Context
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.network.AlbumService
import com.team4.vinilosapp.ui.models.NewAlbum

class AlbumRepository(private val service: AlbumService) {

    fun getAlbums(
        context: Context,
        onResult: (List<Album>) -> Unit,
        onError: (String) -> Unit
    ) {
        service.getAlbums(context, onResult, onError)
    }

    fun createAlbum(
        context: Context,
        album: NewAlbum,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        service.createAlbum(context, album, onSuccess, onError)
    }
}