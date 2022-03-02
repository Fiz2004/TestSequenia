package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.R
import com.fiz.testsequenia.model.DataMovies
import com.fiz.testsequenia.model.MoviesRepository

class MoviesPresenter(
    private val view: IMoviesView,
    private val moviesRepository: MoviesRepository,
    private val dataMovies: DataMovies
) : IMoviesPresenter {

    fun onViewCreated() {
        addObserver()
        initUI()
        if (dataMovies.isLoadData()) {
            if (dataMovies.isGenreSelected())
                onLoadMoviesIfGenreSelected()
            else
                onLoadMovies()
        }
    }

    private fun addObserver() {
        moviesRepository.addCallBack(::onLoadMovies)
    }

    private fun initUI() {
        view.onSetTopAppBarTitle(R.string.main)
        view.initUI()
    }

    fun clickMovie(id: Int) {
        view.clickMovie(id)
    }

    fun clickGenre(genre: String?) {
        if (dataMovies.genreSelected == genre) {
            dataMovies.genreSelected = null
        } else {
            dataMovies.genreSelected = genre
        }
        updateUI()
    }

    override fun onLoadMovies() {
        updateUI()
    }

    override fun onLoadMoviesIfGenreSelected() {
        updateUI()
    }

    private fun updateUI() {
        view.updateUI(dataMovies)
    }

    fun onSaveInstanceState(outState: Bundle) {
        dataMovies.genreSelected?.let { outState.putString(KEY_GENRE_SELECTED, it) }
    }

    fun setGenreSelected(genreSelected: String) {
        dataMovies.genreSelected = genreSelected
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}