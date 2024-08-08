package com.example.rickandmorty.data

import com.example.rickandmorty.CharactersQuery
import com.example.rickandmorty.domain.models.Character
import com.example.rickandmorty.domain.models.Episode
import com.example.rickandmorty.domain.models.Location
import com.example.rickandmorty.domain.models.Resident

fun CharactersQuery.Result.toDomain() = Character(
    name = name.orEmpty(),
    id = id.orEmpty(),
    image = image.orEmpty(),
    status = status.orEmpty(),
    species = species.orEmpty(),
    episode = episode.map { it?.toDomain() ?: Episode("No appearances.") },
    location = location?.toDomain() ?: Location(
        name = "No location available.",
        residents = emptyList()
    )
)

fun CharactersQuery.Episode.toDomain() = Episode(name = name.orEmpty())

fun CharactersQuery.Location.toDomain() = Location(
    name = name.orEmpty(),
    residents = residents.map {
        it?.toDomain() ?: Resident(
            name = "No resident available.",
            image = "No image available.",
            id = "No ID available."
        )
    }
)

fun CharactersQuery.Resident.toDomain() = Resident(
    name = name.orEmpty(),
    image = image.orEmpty(),
    id = id.orEmpty(),
)