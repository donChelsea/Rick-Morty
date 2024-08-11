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
        updateState(ScreenData.Loading)

        viewModelScope.launch {
            updateState(ScreenData.Data(getCharactersUseCase()))
        }
    }

    private fun updateState(
        screenData: ScreenData
    ) = _state.update { it.copy(screenData = screenData) }
}

@Immutable
data class CharactersState(
    val screenData: ScreenData = ScreenData.Initial
)

@Immutable
sealed class ScreenData {
    data object Initial : ScreenData()
    data object Loading : ScreenData()

    @Immutable
    data class Data(
        val characters: List<Character> = emptyList(),
    ) : ScreenData()
}
