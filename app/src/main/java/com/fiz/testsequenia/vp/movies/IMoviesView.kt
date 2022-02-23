package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.network.models.MovieProperty

interface IMoviesView {
    fun showMovies(genres: List<String>, sortMovies: List<MovieProperty>, genreSelected: String? = null)
}