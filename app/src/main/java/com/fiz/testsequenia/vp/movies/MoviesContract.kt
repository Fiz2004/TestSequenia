package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

interface MoviesContract {

    interface View {
        fun moveMovieDetails(id: Int)
        fun updateUI(
            movies: List<Movie>, genres: List<Genre>
        )

        fun showError(message: String)
        fun setLoadingIndicator(active: Boolean)
    }

    interface Presenter {

        fun loadMovies()

        fun clickMovie(id: Int)

        fun clickGenre(genre: Genre?)

        fun setGenreSelected1(genreSelected: Genre)

        fun onSaveInstanceState(outState: Bundle)
    }
}