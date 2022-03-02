package com.fiz.testsequenia.model

import com.fiz.testsequenia.model.network.models.MovieProperty

class DataMovies(
    private val moviesRepository: MoviesRepository,
    var genreSelected: String? = null
) {
    val genres: List<String>?
        get() = moviesRepository.getGenres()

    private val sortMovies: List<MovieProperty>?
        get() = moviesRepository.getSortMovies()

    fun getMovies(): List<MovieProperty>? = if (genreSelected == null)
        sortMovies
    else
        getFilterMovies()


    private fun getFilterMovies() = sortMovies?.filter {
        it.genres.contains(genreSelected)
    }

    fun isGenreSelected(): Boolean {
        if (genreSelected == null)
            return false
        return true
    }
}