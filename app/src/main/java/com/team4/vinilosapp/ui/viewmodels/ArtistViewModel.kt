package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.ArtistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer

class ArtistViewModel(application: Application) : AndroidViewModel(application) {

    private val serviceAdapter = VinilosServiceAdapterImpl(RetrofitProvider.api)
    private val repository = ArtistRepository(serviceAdapter)

    private val _originalArtists = MutableStateFlow<List<Performer>>(emptyList())
    private val _artists = MutableStateFlow<List<Performer>>(emptyList())
    val artists: StateFlow<List<Performer>> = _artists

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchArtists() {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getArtists()
                .onSuccess { list ->
                    _originalArtists.value = list
                    _artists.value = list
                }
                .onFailure { error ->
                    Log.e("ArtistViewModel", error.message ?: "Error al obtener artistas")
                }

            _isLoading.value = false
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