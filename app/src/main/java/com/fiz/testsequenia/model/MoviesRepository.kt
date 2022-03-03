package com.fiz.testsequenia.model

import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MovieProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesRepository {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    var message: String = ""

    fun loadDataMovies(callBack: () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val listResult = MoviesApi.retrofitService.getProperties()

                message = ""
                genres = listResult.films.flatMap { movie -> movie.genres }.distinct()

                val movies = listResult.films
                sortMovies = movies.sortedBy { it.localizedName }
            } catch (e: Exception) {
                message = e.message.toString()
            }
            withContext(Dispatchers.Main) {
                callBack()
            }
        }
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