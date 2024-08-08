package com.example.rickandmorty.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.domain.models.Character
import com.example.rickandmorty.domain.usecases.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersState())
    val state = _state.asStateFlow()

    init {
        updateState(isLoading = true)

        viewModelScope.launch {
            updateState(
                characters = getCharactersUseCase(),
                isLoading = false
            )
        }
    }

    private fun updateState(
        characters: List<Character> = _state.value.characters,
        isLoading: Boolean = _state.value.isLoading,
    ) = _state.update {
        it.copy(
            characters = characters,
            isLoading = isLoading
        )
    }
}

@Immutable
data class CharactersState(
    val characters: List<Character> = emptyList(),
    val isLoading: Boolean = false
)
