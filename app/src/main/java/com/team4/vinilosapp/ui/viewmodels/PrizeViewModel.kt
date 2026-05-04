package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapterImpl
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

    fun resetCreateState() {
        _createSuccess.value = false
        _createError.value = null
    }
}