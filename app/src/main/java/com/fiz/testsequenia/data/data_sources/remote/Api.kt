package com.fiz.testsequenia.data.data_sources.remote

import com.fiz.testsequenia.data.data_sources.remote.dto.MoviesDto
import retrofit2.http.GET

interface MoviesApi {
    @GET("sequeniatesttask/films.json")
    suspend fun fetchMovies(): MoviesDto

    companion object {
        const val BASE_URL =
            "https://s3-eu-west-1.amazonaws.com"
    }
}