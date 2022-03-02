package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.model.DataMovies
import com.fiz.testsequenia.model.MoviesRepository

class MoviesPresenter(
    private val view: IMoviesView,
    private val dataMovies: DataMovies
) {

    fun onViewCreated() {
        MoviesRepository.get().loadDataMovies(::updateUI)
        initUI()
        if (dataMovies.isGenreSelected())
            updateUI()
    }

    private fun initUI() {
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

    fun updateUI() {
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