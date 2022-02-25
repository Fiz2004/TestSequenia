package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty

class MoviesPresenter(val view: IMoviesView, private var genreSelected: String? = null) : IMoviesPresenter {

    private val moviesRepository: MoviesRepository = MoviesRepository.get()
    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    private lateinit var filterMovies: List<MovieProperty>

    fun onCreateView() {
        moviesRepository.addPresenter(this)
        genres = moviesRepository.getGenres()
        sortMovies = moviesRepository.getSortMovies()
        if (genres != null && sortMovies != null)
            onLoadMovies()
        view.initUI()
    }

    override fun onLoadMovies() {
        if (genreSelected != null) {
            genres = moviesRepository.getGenres()
            sortMovies = moviesRepository.getSortMovies()
            filterMovies = sortMovies!!.filter { it.genres.contains(genreSelected) }
        } else {
            genres = moviesRepository.getGenres()
            sortMovies = moviesRepository.getSortMovies()
            view.updateUI(genres!!, sortMovies!!)
        }
    }

    fun clickGenre(genre: String = "") {
        if (genreSelected == genre) {
            genreSelected = null
            filterMovies = sortMovies!!
            view.updateUI(genres!!, filterMovies)
        } else {
            genreSelected = genre
            filterMovies = sortMovies?.filter { it.genres.contains(genreSelected) }!!
            view.updateUI(genres!!, filterMovies, genreSelected)
        }
    }

    fun spanSizeLookup(genres: List<String>) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, genres.size + 1 -> 2
                in 1..genres.size -> 2
                else -> 1
            }
        }

    fun clickMovie(id: Int) {
        (view as MoviesFragment).findNavController()
            .navigate(
                MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            )
    }

    fun onSaveInstanceState(outState: Bundle) {
        genreSelected?.let { outState.putString(KEY_GENRE_SELECTED, it) }
    }

    fun onStart() {
        (view as MoviesFragment).binding.topAppBar.title = view.context?.resources?.getString(R.string.main)
        if (genres != null && sortMovies != null)
            if (this::filterMovies.isInitialized) {
                view.updateUI(genres!!, filterMovies, genreSelected)
            } else {
                view.updateUI(genres!!, sortMovies!!, genreSelected)
            }
    }

    fun onDestroyView() {
        moviesRepository.removePresenter()
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}