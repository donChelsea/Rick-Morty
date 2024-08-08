package com.example.rickandmorty.data

import com.example.rickandmorty.domain.models.Character

interface CharacterClient {
    suspend fun getCharacters(): List<Character>
}