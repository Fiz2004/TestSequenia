package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.model.network.models.MoviesProperty

interface IMoviesView {
    abstract fun showMovies(genres:List<String>,sortMovies: List<MovieProperty>,currentPosition:Int?=null)
}