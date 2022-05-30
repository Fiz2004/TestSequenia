package com.fiz.testsequenia.di

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(): MoviesApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
            .baseUrl(MoviesApi.BASE_URL)
            .build()
            .create(MoviesApi::class.java)
    }

}