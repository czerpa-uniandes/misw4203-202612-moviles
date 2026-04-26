package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.AlbumRepository
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.AlbumFilter
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.text.Normalizer.normalize

sealed interface AlbumDetailUiState {
    object Loading : AlbumDetailUiState
    data class Success(val album: Album) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AlbumRepository = AlbumRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    // Constructor secundario solo para tests
    internal constructor(
        application: Application,
        repository: AlbumRepository
    ) : this(application) {
        this.repository = repository
    }

    private val _originalAlbums = MutableStateFlow<List<Album>>(emptyList())
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _createLoading = MutableStateFlow(false)
    val createLoading: StateFlow<Boolean> = _createLoading

    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess

    private val _createError = MutableStateFlow<String?>(null)

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun fetchAlbums() {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getAlbums()
                .onSuccess { albumList ->
                    _originalAlbums.value = albumList
                    _albums.value = albumList
                }
                .onFailure { error ->
                    runCatching {
                        Log.e("MY MESSAGE", error.message ?: "Error al obtener álbumes")
                    }
                }

            _isLoading.value = false
        }
    }

    fun loadAlbum(albumId: Int) {
        viewModelScope.launch {
            _uiState.value = AlbumDetailUiState.Loading

            repository.getAlbumDetail(albumId)
                .onSuccess { album ->
                    _uiState.value = AlbumDetailUiState.Success(album)
                }
                .onFailure { error ->
                    _uiState.value = AlbumDetailUiState.Error(
                        error.message ?: "Error cargando álbum"
                    )
                }
        }
    }

    fun createAlbum(
        name: String,
        genre: String,
        cover: String,
        description: String,
        releaseDate: String,
        recordLabel: String
    ) {
        viewModelScope.launch {
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

            repository.createAlbum(newAlbum)
                .onSuccess {
                    _createSuccess.value = true
                    fetchAlbums()
                }
                .onFailure { error ->
                    _createError.value = error.message ?: "Error al crear álbum"
                }

            _createLoading.value = false
        }
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

            val matchesQuery = query.isBlank() || albumName.contains(query)
            val matchesGenre = genre.isBlank() || albumGenre.contains(genre)
            val matchesArtist =
                artist.isBlank() || album.performers.any { performer ->
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

    fun addTrack(
        albumId: Int,
        name: String,
        duration: String,
    ) {
        viewModelScope.launch {
            _createLoading.value = true
            _createSuccess.value = false
            _createError.value = null

            val newAddTrack = AddTrack(
                name = name,
                duration = duration
            )

            println("MY MESSAGE ${newAddTrack.toString()}")

            repository.addTrack(albumId, newAddTrack)
                .onSuccess {
                    _createSuccess.value = true
                    loadAlbum(albumId)
                }
                .onFailure { error ->
                    _createError.value = error.message ?: "Error al agregar track"
                }

            _createLoading.value = false
        }
    }
}