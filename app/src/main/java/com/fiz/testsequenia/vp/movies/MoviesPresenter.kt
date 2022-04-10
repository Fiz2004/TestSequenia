package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.model.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesPresenter(
    private val view: IMoviesView,
    private val moviesRepository: MoviesRepository
) {
    private var message = ""
    private var moviesWithGenresWithSelected: MoviesWithGenresWithSelected? =
        MoviesWithGenresWithSelected()

    fun loadData() {
        CoroutineScope(Dispatchers.Default).launch {
            message = ""
            try {
                view.showLoadView()
                moviesWithGenresWithSelected =
                    moviesWithGenresWithSelected?.loadData(moviesRepository.loadData())
            } catch (e: Exception) {
                message = e.message.toString()
            }
            withContext(Dispatchers.Main) {
                if (message == "") {
                    view.hideLoadView()
                    moviesWithGenresWithSelected?.let {
                        view.updateUI(it)
                    }
                } else {
                    view.showError(message)
                }
            }
        }
    }

    fun clickMovie(id: Int) {
        view.moveMovieDetails(id)
    }

    fun clickGenre(genre: String?) {
        moviesWithGenresWithSelected?.let {
            moviesWithGenresWithSelected = it.setGenreSelected(genre)
        }
        moviesWithGenresWithSelected?.let {
            view.updateUI(it)
        }
    }

    fun setGenreSelected(genreSelected: String) {
        moviesWithGenresWithSelected =
            moviesWithGenresWithSelected?.setGenreSelected(genreSelected)
    }

    fun onSaveInstanceState(outState: Bundle) {
        moviesWithGenresWithSelected?.genreSelected?.let {
            outState.putString(KEY_GENRE_SELECTED, it)
        }
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}