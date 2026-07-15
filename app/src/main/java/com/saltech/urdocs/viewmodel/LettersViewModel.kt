package com.saltech.urdocs.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saltech.urdocs.data.GeminiRepository
import com.saltech.urdocs.model.LetterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LettersUiState(
    val isLoading: Boolean = false,
    val generatedLetter: String? = null,
    val error: String? = null
)

class LettersViewModel(
    private val repository: GeminiRepository = GeminiRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(LettersUiState())
    val uiState: StateFlow<LettersUiState> = _uiState

    fun generate(request: LetterRequest) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val letter = repository.generateLetter(request)
                _uiState.value = LettersUiState(isLoading = false, generatedLetter = letter)
            } catch (e: Exception) {
                _uiState.value = LettersUiState(
                    isLoading = false,
                    error = "Hindi na-generate ang letter: ${e.message ?: "unknown error"}"
                )
            }
        }
    }
}
