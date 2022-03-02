package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.DataMovies

interface IMoviesView {
    fun clickMovie(id: Int)
    fun initUI()
    fun updateUI(
        dataMovies: DataMovies
    )
}