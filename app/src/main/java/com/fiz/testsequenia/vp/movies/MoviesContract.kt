package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

interface MoviesContract {

    interface View {
        fun moveMovieDetails(id: Int)
        fun updateUI(
            movies: List<Movie>, genres: List<Genre>, genreSelected: Genre?
        )

        fun showError(message: String)
        fun setLoadingIndicator(active: Boolean)
        fun clickMovie(id: Int)
        fun clickGenre(genre: Genre)
    }

    interface Presenter {

        var genreSelected: Genre?

        fun loadMovies()

        fun cleanUp()

        fun clickMovie(id: Int)

        fun clickGenre(genre: Genre?)
    }
}