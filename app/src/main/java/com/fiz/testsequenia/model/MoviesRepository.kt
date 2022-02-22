package com.fiz.testsequenia.model

import android.content.Context
import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MoviesProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesRepository(private val context: Context) {

    var listResult: MoviesProperty? = null
    var presenter: IPresenter? = null
    var start = false

    fun loadDataMovies() {
        if (listResult == null)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    start = true
                    listResult = MoviesApi.retrofitService.getProperties()
                    presenter?.loadMovies(listResult!!)
                } catch (e: Exception) {
                    e.message
                }

            }
    }

    fun getDataMovies(): MoviesProperty? {
        if (!start) {
            loadDataMovies()
            return null
        }
        else {
            return listResult}
    }

    fun addPresenter(moviesPresenter: IPresenter) {
        presenter = moviesPresenter
    }

    companion object {
        private var INSTANCE: MoviesRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = MoviesRepository(context)
            }
        }

        fun get(): MoviesRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}