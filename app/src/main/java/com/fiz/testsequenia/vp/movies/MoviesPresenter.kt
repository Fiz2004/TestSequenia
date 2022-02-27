package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty

class MoviesPresenter(
    private val view: IMoviesView,
    private var genreSelected: String? = null,
    private val moviesRepository: MoviesRepository
) : IMoviesPresenter {
    private var genres: List<String>? = null
    private var sortMovies: List<MovieProperty>? = null

    private lateinit var adapter: MoviesAdapter
    private lateinit var manager: GridLayoutManager

    private lateinit var filterMovies: List<MovieProperty>

    fun onCreateView() {
        moviesRepository.addPresenter(this)
        genres = moviesRepository.getGenres()
        sortMovies = moviesRepository.getSortMovies()

        if (genres != null && sortMovies != null) {
            if (genreSelected == null)
                onLoadMovies()
            else
                onLoadMoviesIfGenreSelected()
        }
        initUI()
    }

    override fun onLoadMovies() {
        genres = moviesRepository.getGenres()
        sortMovies = moviesRepository.getSortMovies()

        updateUI(genres!!, sortMovies!!)
    }

    override fun onLoadMoviesIfGenreSelected() {
        genres = moviesRepository.getGenres()
        sortMovies = moviesRepository.getSortMovies()
        filterMovies = sortMovies!!.filter { it.genres.contains(genreSelected) }
    }

    private fun clickGenre(genre: String = "") {
        if (genreSelected == genre) {
            genreSelected = null
            filterMovies = sortMovies!!
            updateUI(genres!!, filterMovies)
        } else {
            genreSelected = genre
            filterMovies = sortMovies?.filter { it.genres.contains(genreSelected) }!!
            updateUI(genres!!, filterMovies, genreSelected)
        }
    }

    private fun spanSizeLookup(genres: List<String>) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, genres.size + 1 -> 2
                in 1..genres.size -> 2
                else -> 1
            }
        }

    private fun clickMovie(id: Int) {
        (view as MoviesFragment).findNavController()
            .navigate(
                MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
            )
    }

    fun onSaveInstanceState(outState: Bundle) {
        genreSelected?.let { outState.putString(KEY_GENRE_SELECTED, it) }
    }

    fun onStart() {
        if (genres != null && sortMovies != null)
            if (this::filterMovies.isInitialized) {
                updateUI(genres!!, filterMovies, genreSelected)
            } else {
                updateUI(genres!!, sortMovies!!, genreSelected)
            }
    }

    fun onDestroyView() {
        moviesRepository.removePresenter()
    }

    private fun updateUI(genres: List<String>, sortMovies: List<MovieProperty>, genreSelected: String? = null) {
        if (!this::adapter.isInitialized) return
        val state = (view as MoviesFragment).binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        adapter.refreshData(
            genres,
            sortMovies,
            genreSelected
        )
        manager.spanSizeLookup = spanSizeLookup(genres)

        view.binding.moviesRecyclerView.post {
            view.binding.moviesRecyclerView.layoutManager = manager
            view.binding.moviesRecyclerView.adapter = adapter
            (view.binding.moviesRecyclerView.layoutManager as GridLayoutManager).onRestoreInstanceState(
                state
            )
        }
    }

    private fun initUI() {
        view.onSetTopAppBarTitle(R.string.main)

        adapter = MoviesAdapter(
            (view as MoviesFragment).requireContext(),
            ::clickMovie,
            ::clickGenre
        )

        manager = GridLayoutManager(view.requireActivity(), 2)
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}