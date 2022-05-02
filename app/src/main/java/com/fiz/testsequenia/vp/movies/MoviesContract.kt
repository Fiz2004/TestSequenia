package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected

interface MoviesContract {

    interface View {
        fun moveMovieDetails(id: Int)
        fun updateUI(
            moviesWithGenresWithSelected: MoviesWithGenresWithSelected
        )

        fun showError(message: String)
        fun setLoadingIndicator(active: Boolean)
    }

    interface Presenter {

        fun loadMovies()

        fun clickMovie(id: Int)

        fun clickGenre(genre: Genre?)

        fun setGenreSelected(genreSelected: Genre)

        fun onSaveInstanceState(outState: Bundle)
    }
}