package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.vp.models.DataItem

interface MoviesContract {

    interface View {
        fun moveMovieDetails(id: Int)

        fun setStateLoading(value: Boolean)
        fun setStateShowMovies(dataItem: List<DataItem>, refreshVisible: Boolean = false)
        fun setStateShowLocalMovies(dataItem: List<DataItem>, message: String?)

        fun setStateFullError(message: String?)

        fun clickMovie(id: Int)
        fun clickGenre(genre: Genre)
    }

    interface Presenter {

        fun loadGenreSelected(genreSelected: String?)

        fun getGenreSelectedName(): String?

        fun loadMovies(fetchFromRemote: Boolean = false)

        fun cleanUp()

        fun clickMovie(id: Int)

        fun clickGenre(genre: Genre?)

        fun getSpanSize(dataItem: List<DataItem>, position: Int): Int
    }
}