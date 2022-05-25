package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.remote.MoviesApi
import com.fiz.testsequenia.data.data_sources.remote.dto.toMovie
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.fiz.testsequenia.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(
    private val moviesApi: MoviesApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MoviesRepository {
    override var movies: List<Movie>? = null; private set

    override suspend fun loadData(): Resource<List<Movie>?> = withContext(dispatcher) {
        try {
            if ((movies?.isEmpty() == true) || movies == null) {
                val response = moviesApi.fetchMovies()
                movies = response.films?.mapNotNull { it }?.map { it.toMovie() } ?: listOf()
            }
            Resource.Success(movies)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}