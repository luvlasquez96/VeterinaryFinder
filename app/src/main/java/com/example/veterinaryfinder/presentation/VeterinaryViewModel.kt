package com.example.veterinaryfinder.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veterinaryfinder.dataAccess.VeterinaryRepository
import com.example.veterinaryfinder.model.Veterinary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VeterinaryViewModel @Inject constructor(private val repository: VeterinaryRepository) :
    ViewModel() {

    private val _viewState = MutableStateFlow<VeterinaryViewState>(VeterinaryViewState.Loading)
    val viewState: StateFlow<VeterinaryViewState> = _viewState

    private var cachedVeterinaries: List<Veterinary> = emptyList()

    init {
        loadVeterinaries()
    }

    private fun loadVeterinaries(query: String = "") {
        viewModelScope.launch {
            _viewState.value = VeterinaryViewState.Loading
            try {
                repository.searchVeterinaries(query).collect { veterinaries ->
                    Timber.tag("VeterinaryViewModel").d("Loaded veterinarians: %s", veterinaries)
                    cachedVeterinaries = veterinaries
                    _viewState.value = VeterinaryViewState.Success(veterinaries)
                }
            } catch (e: Exception) {
                _viewState.value = VeterinaryViewState.Error("Failed to load veterinaries")
            }
        }
    }

    fun deleteVeterinary(veterinary: Veterinary) {
        viewModelScope.launch {
            try {
                repository.deleteVeterinary(veterinary)
                cachedVeterinaries = cachedVeterinaries - veterinary
                _viewState.value = VeterinaryViewState.Success(cachedVeterinaries)
            } catch (e: Exception) {
                _viewState.value = VeterinaryViewState.Error("Failed to delete veterinary")
            }
        }
    }

    fun loadVeterinary(veterinaryId: Int, onLoaded: (Veterinary) -> Unit) {
        viewModelScope.launch {
            try {
                val veterinary = repository.getVeterinaryById(veterinaryId)
                onLoaded(veterinary)
            } catch (e: Exception) {
                Timber.tag("VeterinaryViewModel").e(e, "Failed to load veterinary")
            }
        }
    }
    fun updateVeterinary(
        veterinaryId: String,
        name: String,
        address: String,
        phone: String,
        webSite: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            val id = veterinaryId.toIntOrNull() ?: return@launch

            val updatedVeterinary = Veterinary(
                id = id,
                name = name,
                address = address,
                phone = phone,
                webSite = webSite,
                image = imageUri
            )

            try {
                repository.updateVeterinary(updatedVeterinary)
                cachedVeterinaries = cachedVeterinaries.map { if (it.id == id) updatedVeterinary else it }
                _viewState.value = VeterinaryViewState.Success(cachedVeterinaries)
            } catch (e: Exception) {
                _viewState.value = VeterinaryViewState.Error("Failed to update veterinary")
            }
        }
    }

    fun addVeterinary(
        name: String,
        address: String,
        phone: String,
        webSite: String,
        imageUri: String?
    ) {
        viewModelScope.launch {
            val newVeterinary = Veterinary(
                name = name,
                address = address,
                phone = phone,
                webSite = webSite,
                image = imageUri
            )

            try {
                repository.insertVeterinary(newVeterinary)
                cachedVeterinaries = cachedVeterinaries + newVeterinary
                _viewState.value = VeterinaryViewState.Success(cachedVeterinaries)
            } catch (e: Exception) {
                _viewState.value = VeterinaryViewState.Error("Failed to add veterinary")
            }
        }
    }

    sealed class VeterinaryViewState {
        object Loading : VeterinaryViewState()
        data class Success(val veterinaries: List<Veterinary>) : VeterinaryViewState()
        data class Error(val message: String) : VeterinaryViewState()
    }
}