package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.CollectorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer

class CollectorViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: CollectorRepository = CollectorRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    internal constructor(
        application: Application,
        repository: CollectorRepository
    ) : this(application) {
        this.repository = repository
    }

    private val _originalCollectors = MutableStateFlow<List<Collector>>(emptyList())
    private val _collectors = MutableStateFlow<List<Collector>>(emptyList())
    val collectors: StateFlow<List<Collector>> = _collectors

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedCollector = MutableStateFlow<CollectorDetail?>(null)
    val selectedCollector: StateFlow<CollectorDetail?> = _selectedCollector

    private val _detailLoading = MutableStateFlow(false)
    val detailLoading: StateFlow<Boolean> = _detailLoading

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    fun fetchCollectors() {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getCollectors()
                .onSuccess { list ->
                    _originalCollectors.value = list
                    _collectors.value = list
                }
                .onFailure { error ->
                    runCatching {
                        Log.e("CollectorViewModel", error.message ?: "Error al obtener coleccionistas")
                    }
                }

            _isLoading.value = false
        }
    }

    fun fetchCollectorById(collectorId: Int) {
        viewModelScope.launch {
            _detailLoading.value = true
            _detailError.value = null

            repository.getCollectorById(collectorId)
                .onSuccess { collector ->
                    _selectedCollector.value = collector
                }
                .onFailure { error ->
                    _detailError.value = error.message ?: "Error al obtener el coleccionista"
                    Log.e("CollectorViewModel", error.message ?: "Error al obtener el coleccionista")
                }

            _detailLoading.value = false
        }
    }

    fun search(query: String) {
        val normalized = normalize(query)
        _collectors.value = if (normalized.isBlank()) {
            _originalCollectors.value
        } else {
            _originalCollectors.value.filter { normalize(it.name).contains(normalized) }
        }
    }

    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}
