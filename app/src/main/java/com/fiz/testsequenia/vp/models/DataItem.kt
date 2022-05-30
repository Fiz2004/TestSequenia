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


class DataDiffCallback(private val oldList: List<DataItem>, private val newList: List<DataItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition] is DataItem.HeaderItem && newList[newItemPosition] is DataItem.HeaderItem ->
                (oldList[oldItemPosition] as DataItem.HeaderItem).title == (newList[newItemPosition] as DataItem.HeaderItem).title

            oldList[oldItemPosition] is DataItem.GenreItem && newList[newItemPosition] is DataItem.GenreItem ->
                (oldList[oldItemPosition] as DataItem.GenreItem).genre == (newList[newItemPosition] as DataItem.GenreItem).genre

            oldList[oldItemPosition] is DataItem.MovieItem && newList[newItemPosition] is DataItem.MovieItem ->
                (oldList[oldItemPosition] as DataItem.MovieItem).movie.id == (newList[newItemPosition] as DataItem.MovieItem).movie.id

            else ->
                false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

}