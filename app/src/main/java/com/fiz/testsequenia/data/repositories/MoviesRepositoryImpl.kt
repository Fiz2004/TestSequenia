package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.data_sources.remote.dto.MovieDto
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected
import com.fiz.testsequenia.domain.repositories.MoviesRepository

class MoviesRepositoryImpl(private val moviesApi: MoviesApi) : MoviesRepository {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieDto>? = null

    override suspend fun loadData(): MoviesWithGenresWithSelected {
        if ((genres?.isEmpty() == true || genres == null)
            && (sortMovies?.isEmpty() == true) || sortMovies == null
        ) {
            val listResult = moviesApi.fetchMovies()

            genres = listResult.films.flatMap { movie -> movie.genres }.distinct()

            val movies = listResult.films
            sortMovies = movies.sortedBy { it.localizedName }
        }
        return MoviesWithGenresWithSelected(
            genres = genres ?: listOf(),
            sortMovies = sortMovies ?: listOf()
        )
    }

    override fun getGenres(): List<String> {
        return genres ?: listOf()
    }

    override fun getSortMovies(): List<MovieDto> {
        return sortMovies ?: listOf()
    }

    companion object {
        private var INSTANCE: MoviesRepositoryImpl? = null
        fun initialize(moviesApi: MoviesApi) {
            if (INSTANCE == null) {
                INSTANCE = MoviesRepositoryImpl(moviesApi)
            }
        }

        fun get(): MoviesRepositoryImpl {
            return INSTANCE
                ?: throw IllegalStateException("MoviesRepository must be initialized")
        }
    }
}