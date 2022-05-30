package com.fiz.testsequenia.data.repositories

import com.fiz.testsequenia.data.data_sources.local.MovieDatabase
import com.fiz.testsequenia.data.data_sources.local.toMovie
import com.fiz.testsequenia.data.data_sources.local.toMovieEntity
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
    private val movieDatabase: MovieDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : MoviesRepository {
    override var movies: List<Movie>? = null; private set
    var isLoadRemote: Boolean = false

    override suspend fun loadData(fetchFromRemote: Boolean): Resource<List<Movie>?> =
        withContext(dispatcher) {

            if (movies.isNullOrEmpty() || fetchFromRemote) {
                val response = try {
                    moviesApi.fetchMovies()
                } catch (e: Exception) {
                    movies = movieDatabase.dao.getAll().map { it.toMovie() }
                    return@withContext if (movies.isNullOrEmpty())
                        Resource.Error(e.message.toString())
                    else
                        Resource.SuccessOnlyLocal(movies)
                }
                movies = response.films?.mapNotNull { it }?.map { it.toMovie() }
                movies?.let {
                    movieDatabase.dao.clearAll()
                    movieDatabase.dao.insertAll(it.map { movie -> movie.toMovieEntity() })
                }
                movies = movieDatabase.dao.getAll().map { it.toMovie() }
                isLoadRemote = true
            }

            return@withContext if (isLoadRemote)
                Resource.Success(movies)
            else
                Resource.SuccessOnlyLocal(movies)
        }
}