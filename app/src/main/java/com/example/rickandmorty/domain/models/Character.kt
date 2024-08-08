package com.example.rickandmorty.domain.models

data class Character(
    val name: String,
    val id: String,
    val image: String,
    val status: String,
    val species: String,
    val episode: List<Episode>,
    val location: Location,
)

data class Episode(
    val name: String
)

data class Location(
    val name: String,
    val residents: List<Resident>,
)

data class Resident(
    val name: String,
    val image: String,
    val id: String,
)