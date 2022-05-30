package com.fiz.testsequenia.vp.models

import androidx.recyclerview.widget.DiffUtil
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

sealed class DataItem {
    data class GenreItem(val genre: Genre, val selected: Boolean) : DataItem()
    data class MovieItem(val movie: Movie) : DataItem()
    data class HeaderItem(val title: String) : DataItem()

    companion object {
        fun getDataItemFromDomain(
            textGenre: String,
            textMovie: String,
            movies: List<Movie>,
            genres: List<Genre>,
            genreSelected: Genre?
        ): List<DataItem> {

            val listHeaderGenres = listOf(HeaderItem(textGenre))
            val listGenres = genres.map { GenreItem(it, it == genreSelected) }
            val listHeaderMovies = listOf(HeaderItem(textMovie))

            val moviesSelected = if (genreSelected != null) {
                movies.filter {
                    it.genres.contains(genreSelected.name)
                }
            } else {
                movies
            }
            val listMovies = moviesSelected.map { MovieItem(it) }

            return listHeaderGenres +
                    listGenres +
                    listHeaderMovies + listMovies


        }
    }
}