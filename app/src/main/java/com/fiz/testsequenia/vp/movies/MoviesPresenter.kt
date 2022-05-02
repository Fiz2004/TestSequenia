package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MoviesPresenter(
    private val view: MoviesContract.View,
    private val moviesRepository: MoviesRepository
) : MoviesContract.Presenter {
    private var message = ""
    var genres: List<Genre> = listOf()
    private var movies: List<Movie> = listOf()
    var genreSelected: Genre? = null

    override fun loadMovies() {
        CoroutineScope(Dispatchers.Default).launch {
            message = ""
            try {
                view.setLoadingIndicator(active = true)
                moviesRepository.loadData { movies, genres ->
                    this@MoviesPresenter.movies = movies
                    this@MoviesPresenter.genres = genres
                    CoroutineScope(Dispatchers.Main).launch {
                        if (message == "") {
                            view.setLoadingIndicator(active = false)
                            view.updateUI(this@MoviesPresenter.movies, genres)
                        } else {
                            view.showError(message)
                        }
                    }
                }
            } catch (e: Exception) {
                message = e.message.toString()
            }
        }
    }

    override fun clickMovie(id: Int) {
        view.moveMovieDetails(id)
    }

    override fun clickGenre(genre: Genre?) {
        if (genre != null) {
            setGenreSelected1(genre)
        }
        view.updateUI(movies, genres)
    }

    override fun setGenreSelected1(genreSelected: Genre) {
        this.genreSelected = if (this.genreSelected == genreSelected) {
            null
        } else {
            genreSelected
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        genreSelected?.let {
            outState.putString(KEY_GENRE_SELECTED, it.name)
        }
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}