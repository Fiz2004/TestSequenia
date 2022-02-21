package com.fiz.testsequenia.model

import com.fiz.testsequenia.model.network.models.MoviesProperty

interface IPresenter {
    fun loadMovies(listResult: MoviesProperty)
}