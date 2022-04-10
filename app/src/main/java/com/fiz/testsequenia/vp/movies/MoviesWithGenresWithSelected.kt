package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.network.models.MovieProperty

data class MoviesWithGenresWithSelected(
    val genres: List<String> = listOf(),
    private val sortMovies: List<MovieProperty> = listOf(),
    val genreSelected: String? = null
) {
    val movies
        get() = if (isGenreSelected())
            getFilterMovies()
        else
            sortMovies

    private fun getFilterMovies() = sortMovies.filter {
        it.genres.contains(genreSelected)
    }

    fun isGenreSelected(): Boolean {
        return (genreSelected != null)
    }

    fun setGenreSelected(genre: String?): MoviesWithGenresWithSelected {
        val genreSelected = if (genreSelected == genre) {
            null
        } else {
            genre
        }
        return copy(genreSelected = genreSelected)
    }

    fun loadData(moviesWithGenresWithSelected: MoviesWithGenresWithSelected): MoviesWithGenresWithSelected {
        return copy(
            genres = moviesWithGenresWithSelected.genres,
            sortMovies = moviesWithGenresWithSelected.sortMovies
        )
    }
}