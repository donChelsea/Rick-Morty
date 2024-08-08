package com.example.rickandmorty.di

import com.apollographql.apollo.ApolloClient
import com.example.rickandmorty.data.CharacterClientImpl
import com.example.rickandmorty.data.CharacterClient
import com.example.rickandmorty.domain.usecases.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient =
        ApolloClient.Builder()
            .serverUrl("https://rickandmortyapi.com/graphql")
            .build()

    @Provides
    @Singleton
    fun provideCharacterClient(apolloClient: ApolloClient): CharacterClient =
        CharacterClientImpl(apolloClient)

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(characterClient: CharacterClient): GetCharactersUseCase =
        GetCharactersUseCase(characterClient)
}