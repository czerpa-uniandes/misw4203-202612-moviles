package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.BandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer

class BandViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: BandRepository = BandRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    internal constructor(
        application: Application,
        repository: BandRepository
    ) : this(application) {
        this.repository = repository
    }

    private val _originalBands = MutableStateFlow<List<Performer>>(emptyList())
    private val _bands = MutableStateFlow<List<Performer>>(emptyList())
    val bands: StateFlow<List<Performer>> = _bands

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedBand = MutableStateFlow<BandDetail?>(null)
    val selectedBand: StateFlow<BandDetail?> = _selectedBand

    private val _detailLoading = MutableStateFlow(false)
    val detailLoading: StateFlow<Boolean> = _detailLoading

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    fun fetchBandDetail(bandId: Int) {
        viewModelScope.launch {
            _detailLoading.value = true
            _detailError.value = null
            repository.getBandDetail(bandId)
                .onSuccess { band -> _selectedBand.value = band }
                .onFailure { error ->
                    _detailError.value = error.message ?: "Error al obtener la banda"
                    Log.e("BandViewModel", _detailError.value!!)
                }
            _detailLoading.value = false
        }
    }

    fun fetchBands() {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getBands()
                .onSuccess { list ->
                    _originalBands.value = list
                    _bands.value = list
                }
                .onFailure { error ->
                    runCatching {
                        Log.e("BandViewModel", error.message ?: "Error al obtener bandas")
                    }
                }

            _isLoading.value = false
        }
    }

    fun search(query: String) {
        val normalized = normalize(query)
        _bands.value = if (normalized.isBlank()) {
            _originalBands.value
        } else {
            _originalBands.value.filter { normalize(it.name).contains(normalized) }
        }
    }

    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}
