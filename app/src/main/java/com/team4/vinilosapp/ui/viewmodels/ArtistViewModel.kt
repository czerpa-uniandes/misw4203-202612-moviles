package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ArtistRepository = ArtistRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    internal constructor(
        application: Application,
        repository: ArtistRepository
    ) : this(application) {
        this.repository = repository
    }

    private val _originalArtists = MutableStateFlow<List<Performer>>(emptyList())
    private val _artists = MutableStateFlow<List<Performer>>(emptyList())
    val artists: StateFlow<List<Performer>> = _artists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedArtist = MutableStateFlow<ArtistDetail?>(null)
    val selectedArtist: StateFlow<ArtistDetail?> = _selectedArtist

    private val _detailLoading = MutableStateFlow(false)
    val detailLoading: StateFlow<Boolean> = _detailLoading

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    fun fetchArtists() {
        viewModelScope.launch {
            val t0 = System.currentTimeMillis()
            Log.d("ArtistPerf", "fetchArtists() iniciado")
            _isLoading.value = true

            repository.getArtists()
                .onSuccess { list ->
                    _originalArtists.value = list
                    _artists.value = list
                }
                .onFailure { e ->
                    _error.value = "Sin conexión. Verifica tu red e intenta de nuevo."
                }

            _isLoading.value = false
        }
    }

    fun fetchArtistDetail(artistId: Int) {
        viewModelScope.launch {
            _detailLoading.value = true
            _detailError.value = null

            repository.getArtistDetail(artistId)
                .onSuccess { artist ->
                    _selectedArtist.value = artist
                }
                .onFailure {
                    _detailError.value = "No fue posible cargar el detalle del artista."
                }

            _detailLoading.value = false
        }
    }

    fun search(query: String) {
        val normalized = normalize(query)
        _artists.value = if (normalized.isBlank()) {
            _originalArtists.value
        } else {
            _originalArtists.value.filter { normalize(it.name).contains(normalized) }
        }
    }

    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}