package com.team4.vinilosapp.ui.screens.albums.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.model.AlbumDetailDto
import com.team4.vinilosapp.data.repository.AlbumRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AlbumDetailUiState {
    object Loading : AlbumDetailUiState
    data class Success(val album: AlbumDetailDto) : AlbumDetailUiState
    data class Error(val message: String) : AlbumDetailUiState
}

class AlbumDetailViewModel(
    private val repository: AlbumRepository = AlbumRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState: StateFlow<AlbumDetailUiState> = _uiState.asStateFlow()

    fun loadAlbum(albumId: Int) {
        viewModelScope.launch {
            _uiState.value = AlbumDetailUiState.Loading
            try {
                val album = repository.getAlbumDetail(albumId)
                _uiState.value = AlbumDetailUiState.Success(album)
            } catch (e: Exception) {
                _uiState.value = AlbumDetailUiState.Error(e.message ?: "Error cargando álbum")
            }
        }
    }
}