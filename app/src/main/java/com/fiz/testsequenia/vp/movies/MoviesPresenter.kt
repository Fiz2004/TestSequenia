package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.model.IPresenter
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MoviesPresenter(private val view: IMoviesView, private var genreSelected: String? = null) : IPresenter {

    private val moviesRepository: MoviesRepository = MoviesRepository.get()
    private lateinit var genres: List<String>
    private lateinit var sortMovies: List<MovieProperty>

    private lateinit var filterMovies: List<MovieProperty>

    init {
        moviesRepository.addPresenter(this)
        val listResult = moviesRepository.getDataMovies()
        if (listResult != null)
            loadMovies(listResult)
    }

    override fun loadMovies(listResult: MoviesProperty) {
        val allGenres: MutableSet<String> = mutableSetOf()

        listResult.films.map { movie -> movie.genres.forEach { allGenres.add(it) } }

        genres = allGenres.distinct()

        val movies = listResult.films
        sortMovies = movies.sortedBy { it.localizedName }

        if (genreSelected != null) {
            filterMovies = sortMovies.filter { it.genres.contains(genreSelected) }
            view.showMovies(genres, filterMovies, genreSelected)
        } else {
            view.showMovies(genres, sortMovies)
        }
    }

    fun destroy() {
    }

    fun clickGenre(genre: String = "") {
        if (genreSelected == genre) {
            genreSelected = null
            filterMovies = sortMovies
            view.showMovies(genres, filterMovies)
        } else {
            genreSelected = genre
            filterMovies = sortMovies.filter { it.genres.contains(genreSelected) }
            view.showMovies(genres, filterMovies, genreSelected)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        genreSelected?.let { outState.putString(KEY_GENRE_SELECTED, it) }
    }

    fun onStart() {
        if (this::genres.isInitialized && this::sortMovies.isInitialized)
            if (this::filterMovies.isInitialized)
                view.showMovies(genres, filterMovies, genreSelected)
            else
                view.showMovies(genres, sortMovies, genreSelected)
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}