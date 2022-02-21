package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.network.models.MoviesProperty

interface IMoviesView {
    abstract fun showMovies(listResult: MoviesProperty)
}