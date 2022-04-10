package com.fiz.testsequenia.model

import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.vp.movies.MoviesWithGenresWithSelected

class MoviesRepository(private val moviesApi: MoviesApi) {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    suspend fun loadData(): MoviesWithGenresWithSelected {
        if ((genres?.isEmpty() == true || genres == null)
            && (sortMovies?.isEmpty() == true) || sortMovies == null
        ) {
            val listResult = moviesApi.retrofitService.fetchMovies()

            genres = listResult.films.flatMap { movie -> movie.genres }.distinct()

            val movies = listResult.films
            sortMovies = movies.sortedBy { it.localizedName }
        }
        return MoviesWithGenresWithSelected(
            genres = genres ?: listOf(),
            sortMovies = sortMovies ?: listOf()
        )
    }

    fun getGenres(): List<String> {
        return genres ?: listOf()
    }

    fun getSortMovies(): List<MovieProperty> {
        return sortMovies ?: listOf()
    }

    companion object {
        private var INSTANCE: MoviesRepository? = null
        fun initialize(moviesApi: MoviesApi) {
            if (INSTANCE == null) {
                INSTANCE = MoviesRepository(moviesApi)
            }
        }

        fun get(): MoviesRepository {
            return INSTANCE
                ?: throw IllegalStateException("MoviesRepository must be initialized")
        }
    }
}