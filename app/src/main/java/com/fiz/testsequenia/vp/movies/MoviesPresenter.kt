package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.model.DataMovies
import com.fiz.testsequenia.model.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MoviesPresenter(
    private val view: IMoviesView,
    private val dataMovies: DataMovies,
    private val moviesRepository: MoviesRepository
) {
    var message = ""

    fun onViewCreated() {
        initUI()
        loadData()
        if (dataMovies.isGenreSelected())
            updateUI()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                message = ""
                moviesRepository.loadData()
            } catch (e: Exception) {
                message = e.message.toString()
            }
            withContext(Dispatchers.Main) {
                updateUI()
            }
        }
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