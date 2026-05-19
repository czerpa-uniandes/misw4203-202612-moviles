package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
import com.team4.vinilosapp.data.models.Prize
import com.team4.vinilosapp.data.network.RetrofitProvider
import com.team4.vinilosapp.data.repository.PrizeRepository
import com.team4.vinilosapp.ui.models.AddPrize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrizeViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PrizeRepository = PrizeRepository(
        VinilosServiceAdapterImpl(RetrofitProvider.api)
    )

    // Para tests (igual que Albums)
    internal constructor(
        application: Application,
        repository: PrizeRepository
    ) : this(application) {
        this.repository = repository
    }

    private val _createLoading = MutableStateFlow(false)
    val createLoading: StateFlow<Boolean> = _createLoading

    private val _createSuccess = MutableStateFlow(false)
    val createSuccess: StateFlow<Boolean> = _createSuccess

    private val _createError = MutableStateFlow<String?>(null)
    val createError: StateFlow<String?> = _createError
    private val _prizes = MutableStateFlow<List<Prize>>(emptyList())
    val prizes: StateFlow<List<Prize>> = _prizes
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _associateLoading = MutableStateFlow(false)
    val associateLoading: StateFlow<Boolean> = _associateLoading

    private val _associateSuccess = MutableStateFlow(false)
    val associateSuccess: StateFlow<Boolean> = _associateSuccess

    private val _associateError = MutableStateFlow<String?>(null)
    val associateError: StateFlow<String?> = _associateError

    fun fetchPrizes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            repository.getPrizes()
                .onSuccess { list ->
                    _prizes.value = list
                }
                .onFailure { error ->
                    _error.value = error.message ?: "Error al obtener los premios"
                }

            _isLoading.value = false
        }
    }

    fun createPrize(
        name: String,
        description: String,
        organization: String
    ) {
        viewModelScope.launch {
            _createLoading.value = true
            _createSuccess.value = false
            _createError.value = null

            val newPrize = AddPrize(
                name = name,
                description = description,
                organization = organization
            )

            repository.createPrize(newPrize)
                .onSuccess {
                    _createSuccess.value = true
                }
                .onFailure { error ->
                    _createError.value = error.message ?: "Error al crear premio"
                }

            _createLoading.value = false
        }
    }

    fun associatePrizeArtist(
        prizeId: Int,
        artistId: Int,
        premiationDate: String
    ) {
        viewModelScope.launch {
            _associateLoading.value = true
            _associateSuccess.value = false
            _associateError.value = null

            val formattedDate = "${premiationDate}T00:00:00.000Z"

            repository.associatePrizeArtist(
                prizeId = prizeId,
                artistId = artistId,
                premiationDate = formattedDate
            )
                .onSuccess {
                    _associateSuccess.value = true
                }
                .onFailure { error ->
                    _associateError.value = error.message ?: "Error al asociar premio"
                }

            _associateLoading.value = false
        }
    }

    fun resetCreateState() {
        _createSuccess.value = false
        _createError.value = null
    }

    fun resetAssociateState() {
        _associateSuccess.value = false
        _associateError.value = null
    }
}