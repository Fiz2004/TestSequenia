package com.fiz.testsequenia.app

import android.app.Application
import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.repositories.MoviesRepositoryImpl
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AppContainer {

    private fun provideMopshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun provideMoviesApi(): MoviesApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(provideMopshi()))
            .baseUrl(MoviesApi.BASE_URL)
            .build()
            .create(MoviesApi::class.java)
    }

    private fun provideUserRepository(): MoviesRepository {
        MoviesRepositoryImpl.initialize(provideMoviesApi())
        return MoviesRepositoryImpl.get()
    }

    val moviesRepository = provideUserRepository()
}

class App : Application() {
    val appContainer = AppContainer()
}