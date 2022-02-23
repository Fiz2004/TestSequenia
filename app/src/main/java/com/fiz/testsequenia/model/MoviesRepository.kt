package com.fiz.testsequenia.model

import android.content.Context
import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.vp.movies.IMoviesPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesRepository(private val context: Context) {
    private var presenter: IMoviesPresenter? = null

    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    fun loadDataMovies() {
        if (genres == null || sortMovies == null)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val listResult = MoviesApi.retrofitService.getProperties()

                    val allGenres: MutableSet<String> = mutableSetOf()

                    listResult.films.map { movie -> movie.genres.forEach { allGenres.add(it) } }

                    genres = allGenres.distinct()

                    val movies = listResult.films
                    sortMovies = movies.sortedBy { it.localizedName }

                    presenter?.loadMovies()
                } catch (e: Exception) {
                    e.message
                }
            }
    }

    fun getGenres(): List<String>? {
        return if (genres == null) {
            null
        } else {
            genres
        }
    }

    fun getSortMovies(): List<MovieProperty>? {
        return if (sortMovies == null) {
            null
        } else {
            sortMovies
        }
    }

    fun addPresenter(moviesPresenter: IMoviesPresenter) {
        presenter = moviesPresenter
    }

    fun removePresenter() {
        presenter = null
    }

    companion object {
        private var INSTANCE: MoviesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MoviesRepository(context)
                INSTANCE?.loadDataMovies()
            }
        }

        fun get(): MoviesRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}