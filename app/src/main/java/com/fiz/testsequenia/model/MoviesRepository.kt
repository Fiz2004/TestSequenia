package com.fiz.testsequenia.model

import android.content.Context
import com.fiz.testsequenia.model.network.MoviesApi
import com.fiz.testsequenia.model.network.models.MoviesProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesRepository(private val context: Context) {

    var listResult: MoviesProperty? = null
    lateinit var presenter: IPresenter

    fun loadDataMovies() {
        if (listResult == null)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    listResult = MoviesApi.retrofitService.getProperties()
                    presenter.loadMovies(listResult!!)
                } catch (e: Exception) {
                    e.message
                }

            }
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