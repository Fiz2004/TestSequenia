package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesPresenter(
    private val view: MoviesContract.View,
    private val moviesRepository: MoviesRepository
) : MoviesContract.Presenter {
    private var message = ""
    private var moviesWithGenresWithSelected: MoviesWithGenresWithSelected? =
        MoviesWithGenresWithSelected()

    override fun loadMovies() {
        CoroutineScope(Dispatchers.Default).launch {
            message = ""
            try {
                view.setLoadingIndicator(active = true)
                moviesWithGenresWithSelected =
                    moviesWithGenresWithSelected?.loadData(moviesRepository.loadData())
            } catch (e: Exception) {
                message = e.message.toString()
            }
            withContext(Dispatchers.Main) {
                if (message == "") {
                    view.setLoadingIndicator(active = false)
                    moviesWithGenresWithSelected?.let {
                        view.updateUI(it)
                    }
                } else {
                    view.showError(message)
                }
            }
        }
    }

    override fun clickMovie(id: Int) {
        view.moveMovieDetails(id)
    }

    override fun clickGenre(genre: Genre?) {
        moviesWithGenresWithSelected?.let {
            moviesWithGenresWithSelected = it.setGenreSelected(genre)
        }
        moviesWithGenresWithSelected?.let {
            view.updateUI(it)
        }
    }

    override fun setGenreSelected(genreSelected: Genre) {
        moviesWithGenresWithSelected =
            moviesWithGenresWithSelected?.setGenreSelected(genreSelected)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        moviesWithGenresWithSelected?.genreSelected?.let {
            outState.putString(KEY_GENRE_SELECTED, it.name)
        }
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}