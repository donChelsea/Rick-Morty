package com.example.rickandmorty.data

import com.apollographql.apollo.ApolloClient
import com.example.rickandmorty.CharactersQuery
import com.example.rickandmorty.domain.models.Character
import com.example.rickandmorty.domain.models.Location

class CharacterClientImpl(
    private val apolloClient: ApolloClient
): CharacterClient {
    override suspend fun getCharacters(): List<Character> {
        return apolloClient
            .query(CharactersQuery())
            .execute()
            .data
            ?.characters
            ?.results
            ?.map { it?.toDomain() ?: NON_CHARACTER }
            ?: emptyList()
    }

    companion object {
        val NON_CHARACTER = Character(
            name = "N/A",
            id = "N/A",
            image = "N/A",
            status = "N/A",
            species = "N/A",
            location = Location("N/A", emptyList()),
            episode = emptyList()
        )
    }
}