package com.fiz.testsequenia.vp.main

import com.fiz.testsequenia.model.network.MoviesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPresenter {
    fun loadDataMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val listResult = MoviesApi.retrofitService.getProperties()
                listResult.toString()
            } catch (e: Exception) {
                e.message
            }

        }
    }
}