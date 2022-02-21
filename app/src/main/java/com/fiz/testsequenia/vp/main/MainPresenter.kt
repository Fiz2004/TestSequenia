package com.fiz.testsequenia.vp.main

import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.MoviesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPresenter(val view:IMainView) {

    val moviesRepository:MoviesRepository= MoviesRepository.get()

    fun loadDataMovies() {
        moviesRepository.loadDataMovies()
    }

    fun detachView() {

    }

    fun destroy() {

    }

}