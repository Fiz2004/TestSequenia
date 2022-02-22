package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import com.fiz.testsequenia.model.IPresenter
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MoviesPresenter(val view: IMoviesView, var positionSelected: Int? = null) : IPresenter {

    val moviesRepository: MoviesRepository = MoviesRepository.get()
    private lateinit var genres: List<String>
    private lateinit var sortMovies: List<MovieProperty>

    private var genreSelected: List<String>? = null
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

        if (positionSelected != null)
            clickGenre(positionSelected!!)
        else
            view.showMovies(genres, sortMovies)
    }

    fun destroy() {
    }

    fun clickGenre(position: Int) {
        if (genreSelected == listOf(genres[position])) {
            genreSelected = null
            positionSelected = null
            filterMovies = sortMovies
            view.showMovies(genres, filterMovies)
        } else {
            genreSelected = listOf(genres[position])
            filterMovies = sortMovies.filter { it.genres.contains(genreSelected?.get(0)) }
            positionSelected = position
            view.showMovies(genres, filterMovies, position)
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        positionSelected?.let { outState.putInt("position", it) }
    }

    fun onStart() {
        if (this::genres.isInitialized && this::sortMovies.isInitialized)
            if (this::filterMovies.isInitialized)
                view.showMovies(genres, filterMovies, positionSelected)
            else
                view.showMovies(genres, sortMovies, positionSelected)
    }
}