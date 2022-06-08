package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.vp.models.DataItem

interface MoviesContract {

    interface View {
        fun navigateToMovieDetails(id: Int)

        fun setStateLoading(value: Boolean)
        fun setStateShowMovies(dataItem: List<DataItem>)
        fun setStateShowLocalMovies(dataItem: List<DataItem>, message: String?)
        fun setStateFullError(message: String?)
    }

    interface Presenter {
        var refreshItemVisible: Boolean

        fun loadGenreSelected(genreSelected: String?)

        fun getGenreSelectedName(): String?

        fun loadMovies(fetchFromRemote: Boolean = false)

        fun cleanUp()

        fun clickMovieCard(id: Int)

        fun clickGenreButton(genre: Genre?)

        fun getSpanSize(dataItem: List<DataItem>, position: Int): Int
    }
}