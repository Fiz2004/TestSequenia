package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.data_sources.remote.dto.MovieDto
import com.fiz.testsequenia.data.data_sources.remote.dto.toGenre
import com.fiz.testsequenia.data.data_sources.remote.dto.toMovie
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val moviesApi: MoviesApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MoviesRepository {
    override var genres: List<Genre>? = null; private set
    override var movies: List<Movie>? = null; private set

    override suspend fun loadData(callBack: (List<Movie>, List<Genre>) -> Unit) {
        withContext(dispatcher) {
            if ((genres?.isEmpty() == true || genres == null)
                && (movies?.isEmpty() == true) || movies == null
            ) {
                val response = moviesApi.fetchMovies()
                val movies: List<MovieDto> = response.films

                genres = movies.flatMap { movie -> movie.genres }.distinct().map { it.toGenre() }
                this@MoviesRepositoryImpl.movies =
                    movies.sortedBy { it.localizedName }.map { it.toMovie() }
            }
            callBack(
                movies ?: listOf(),
                genres ?: listOf()
            )
        }
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