package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.data_sources.remote.dto.MovieDto
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MoviesRepository {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieDto>? = null

    override suspend fun loadData(): MoviesWithGenresWithSelected = withContext(dispatcher) {
        if ((genres?.isEmpty() == true || genres == null)
            && (sortMovies?.isEmpty() == true) || sortMovies == null
        ) {

            val response = moviesApi.fetchMovies()
            val movies: List<MovieDto> = response.films

            genres = movies.flatMap { movie -> movie.genres }.distinct()
            sortMovies = movies.sortedBy { it.localizedName }
        }
        return@withContext MoviesWithGenresWithSelected(
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