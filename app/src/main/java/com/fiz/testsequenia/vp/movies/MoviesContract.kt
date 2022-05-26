package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

interface MoviesContract {

    interface View {
        fun moveMovieDetails(id: Int)

        fun setStateLoading(active: Boolean)
        fun setStateShowMovies(movies: List<Movie>, genres: List<Genre>, genreSelected: Genre?)
        fun setStateShowLocalMovies(
            movies: List<Movie>,
            genres: List<Genre>,
            genreSelected: Genre?,
            message: String
        )

        fun setStateFullError(message: String)

        fun clickMovie(id: Int)
        fun clickGenre(genre: Genre)
    }

    interface Presenter {

        var genreSelected: Genre?

        fun loadGenreSelected(genreSelected: String?)

        fun getGenreSelected(): String?

        fun loadMovies()

        fun cleanUp()

        fun clickMovie(id: Int)

        fun clickGenre(genre: Genre?)
    }
}