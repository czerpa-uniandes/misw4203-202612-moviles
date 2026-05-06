package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.BandRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.Normalizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BandViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: BandRepository = BandRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    private var defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    internal constructor(
        application: Application,
        repository: BandRepository,
        defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    ) : this(application) {
        this.repository = repository
        this.defaultDispatcher = defaultDispatcher
    }

    private val _originalBands = MutableStateFlow<List<Performer>>(emptyList())
    private val _bands = MutableStateFlow<List<Performer>>(emptyList())
    val bands: StateFlow<List<Performer>> = _bands

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedBand = MutableStateFlow<BandDetail?>(null)
    val selectedBand: StateFlow<BandDetail?> = _selectedBand

    private val _detailLoading = MutableStateFlow(false)
    val detailLoading: StateFlow<Boolean> = _detailLoading

    private val _detailError = MutableStateFlow<String?>(null)
    val detailError: StateFlow<String?> = _detailError

    private val _allMusicians = MutableStateFlow<List<Performer>>(emptyList())
    val allMusicians: StateFlow<List<Performer>> = _allMusicians

    private val _musiciansLoading = MutableStateFlow(false)
    val musiciansLoading: StateFlow<Boolean> = _musiciansLoading

    private val _addMusicianLoading = MutableStateFlow(false)
    val addMusicianLoading: StateFlow<Boolean> = _addMusicianLoading

    private val _addMusicianError = MutableStateFlow<String?>(null)
    val addMusicianError: StateFlow<String?> = _addMusicianError

    fun fetchAllMusicians() {
        viewModelScope.launch {
            _musiciansLoading.value = true
            repository.getMusicians()
                .onSuccess { list -> _allMusicians.value = list }
                .onFailure { }
            _musiciansLoading.value = false
        }
    }

    fun addMusicianToBand(bandId: Int, musicianId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _addMusicianLoading.value = true
            _addMusicianError.value = null
            repository.addMusicianToBand(bandId, musicianId)
                .onSuccess {
                    fetchBandDetail(bandId)
                    onSuccess()
                }
                .onFailure { error ->
                    _addMusicianError.value = error.message ?: "Error al asociar el músico"
                }
            _addMusicianLoading.value = false
        }
    }

    fun fetchBandDetail(bandId: Int) {
        viewModelScope.launch {
            _detailLoading.value = true
            _detailError.value = null
            repository.getBandDetail(bandId)
                .onSuccess { band -> _selectedBand.value = band }
                .onFailure { error ->
                    _detailError.value = error.message ?: "Error al obtener la banda"
                }
            _detailLoading.value = false
        }
    }

    fun fetchBands() {
        viewModelScope.launch {
            _isLoading.value = true

            repository.getBands()
                .onSuccess { list ->
                    _error.value = null
                    _originalBands.value = list
                    _bands.value = list
                }
                .onFailure { e ->
                    _error.value = "Sin conexión. Verifica tu red e intenta de nuevo."
                }

            _isLoading.value = false
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val filtered = withContext(defaultDispatcher) {
                val normalized = normalize(query)

                if (normalized.isBlank()) {
                    _originalBands.value
                } else {
                    _originalBands.value.filter {
                        normalize(it.name).contains(normalized)
                    }
                }
            }

            _bands.value = filtered
        }
    }

    private fun normalize(text: String): String {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
            .trim()
    }
}
