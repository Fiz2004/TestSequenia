package com.fiz.testsequenia.model

import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MovieProperty

class MoviesRepository {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    suspend fun loadData() {
        val listResult = MoviesApi.retrofitService.getProperties()

        genres = listResult.films.flatMap { movie -> movie.genres }.distinct()

        val movies = listResult.films
        sortMovies = movies.sortedBy { it.localizedName }
    }

    fun getGenres(): List<String>? {
        return genres
    }

    fun getSortMovies(): List<MovieProperty>? {
        return sortMovies
    }

    companion object {
        private var INSTANCE: MoviesRepository? = null
        fun initialize() {
            if (INSTANCE == null) {
                INSTANCE = MoviesRepository()
            }
        }

        fun get(): MoviesRepository {
            return INSTANCE
                ?: throw IllegalStateException("MoviesRepository must be initialized")
        }
    }
}