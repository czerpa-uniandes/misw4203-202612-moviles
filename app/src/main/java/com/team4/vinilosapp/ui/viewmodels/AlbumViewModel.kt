package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.network.AlbumService
import com.team4.vinilosapp.data.repository.AlbumRepository
import com.team4.vinilosapp.ui.models.AlbumFilter
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.Normalizer
import java.text.Normalizer.normalize

class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AlbumRepository(AlbumService())
    private val _originalAlbums = MutableStateFlow<List<Album>>(emptyList())
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums
    private val _isLoading = MutableStateFlow(false)
    private val _createLoading = MutableStateFlow(false)
    val createLoading: StateFlow<Boolean> = _createLoading
    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess
    private val _createError = MutableStateFlow<String?>(null)
    val createError: StateFlow<String?> = _createError
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchAlbums() {
        _isLoading.value = true

        repository.getAlbums(
            getApplication(),
            onResult = {
                _originalAlbums.value = it
                _albums.value = it
                _isLoading.value = false
            },
            onError = {
                _isLoading.value = false
            }
        )
    }

    fun createAlbum(
        name: String,
        genre: String,
        cover: String,
        description: String,
        releaseDate: String,
        recordLabel: String
    ) {
        _createLoading.value = true
        _createSuccess.value = false
        _createError.value = null

        val newAlbum = NewAlbum(
            title = name,
            genre = genre,
            cover = cover,
            description = description,
            releaseDate = releaseDate,
            recordLabel = recordLabel
        )

        Log.d("MY MESSAGE", newAlbum.toString())

        repository.createAlbum(
            getApplication(),
            newAlbum,
            onSuccess = {
                _createLoading.value = false
                _createSuccess.value = true
                fetchAlbums()
            },
            onError = { message ->
                _createLoading.value = false
                _createError.value = message
            }
        )
    }

    fun resetCreateState() {
        _createSuccess.value = false
        _createError.value = null
    }

    fun updateFilter(filter: AlbumFilter) {

        val query = normalize(filter.query)
        val genre = normalize(filter.genre)
        val artist = normalize(filter.artist)

        val filtered = _originalAlbums.value.filter { album ->

            val albumName = normalize(album.name)
            val albumGenre = normalize(album.genre)

            val matchesQuery =
                query.isBlank() || albumName.contains(query)

            val matchesGenre =
                genre.isBlank() || albumGenre.contains(genre)

            val matchesArtist =
                artist.isBlank() ||
                        album.performers.any { performer ->
                            normalize(performer.name).contains(artist)
                        }

            matchesQuery && matchesGenre && matchesArtist
        }

        _albums.value = filtered
    }

    fun normalize(text: String): String {
        return normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}