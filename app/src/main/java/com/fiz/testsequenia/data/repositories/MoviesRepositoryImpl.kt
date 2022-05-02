package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.data_sources.remote.dto.MovieDto
import com.fiz.testsequenia.data.data_sources.remote.dto.toGenre
import com.fiz.testsequenia.data.data_sources.remote.dto.toMovie
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MoviesRepository {
    private var genres: List<Genre>? = null
    private var sortMovies: List<Movie>? = null

    override suspend fun loadData(): MoviesWithGenresWithSelected = withContext(dispatcher) {
        if ((genres?.isEmpty() == true || genres == null)
            && (sortMovies?.isEmpty() == true) || sortMovies == null
        ) {

            val response = moviesApi.fetchMovies()
            val movies: List<MovieDto> = response.films

            genres = movies.flatMap { movie -> movie.genres }.distinct().map { it.toGenre() }
            sortMovies = movies.sortedBy { it.localizedName }.map { it.toMovie() }
        }
        return@withContext MoviesWithGenresWithSelected(
            genres = genres ?: listOf(),
            sortMovies = sortMovies ?: listOf()
        )
    }

    override fun getGenres(): List<Genre> {
        return genres ?: listOf()
    }

    override fun getSortMovies(): List<Movie> {
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