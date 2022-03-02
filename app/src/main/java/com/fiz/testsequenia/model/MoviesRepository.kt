package com.fiz.testsequenia.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.vp.movies.IMoviesPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesRepository(private val context: Context) {
    private var presenter: IMoviesPresenter? = null

    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    suspend fun loadDataMovies() {
        var message: String
        for (n in 0..10) {
            try {
                val listResult = MoviesApi.retrofitService.getProperties()

                genres = listResult.films.flatMap { movie -> movie.genres }.distinct()

                val movies = listResult.films
                sortMovies = movies.sortedBy { it.localizedName }

                withContext(Dispatchers.Main) {
                    presenter?.onLoadMovies()
                }
                break
            } catch (e: Exception) {
                message = e.message.toString()
            }
            if (message != "")
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.loadDataMovies()
                }
            }
        }

        fun get(): MoviesRepository {
            return INSTANCE
                ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}