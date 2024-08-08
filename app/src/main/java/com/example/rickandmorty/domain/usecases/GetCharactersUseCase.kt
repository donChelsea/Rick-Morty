package com.example.rickandmorty.domain.usecases

import com.example.rickandmorty.data.CharacterClient
import com.example.rickandmorty.domain.models.Character

class GetCharactersUseCase(
    private val characterClient: CharacterClient
) {
    suspend fun invoke(): List<Character> {
        return characterClient.getCharacters()
    }
}