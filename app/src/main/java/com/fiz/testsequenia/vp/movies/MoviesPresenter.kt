package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.data.data_sources.remote.dto.toGenre
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.fiz.testsequenia.utils.Resource
import kotlinx.coroutines.*

class MoviesPresenter(
    private val view: MoviesContract.View,
    private val moviesRepository: MoviesRepository
) : MoviesContract.Presenter {
    private var genres: List<Genre> = listOf()
    private var movies: List<Movie> = listOf()

    override var genreSelected: Genre? = null
        set(value) {
            field = if (value == genreSelected) {
                null
            } else {
                value
            }
        }

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun loadMovies() {
        scope.launch {
            view.setLoadingIndicator(active = true)
            val resultLoad = try {
                moviesRepository.loadData()
            } catch (e: Exception) {
                Resource.Error("Network request failed")
            }
            when (resultLoad) {
                is Resource.Success -> {
                    val movies = resultLoad.data ?: listOf()
                    this@MoviesPresenter.movies = movies.sortedBy { it.localizedName }
                    this@MoviesPresenter.genres =
                        movies.flatMap { movie -> movie.genres.map { it } }
                            .distinct().map { it.toGenre() }
                    view.setLoadingIndicator(active = false)
                    view.updateUI(this@MoviesPresenter.movies, genres, genreSelected)
                }
                else -> {
                    view.showError(resultLoad.message!!)
                }
            }
        }
    }

    override fun cleanUp() {
        scope.cancel()
    }

    override fun clickMovie(id: Int) {
        view.moveMovieDetails(id)
    }

    override fun clickGenre(genre: Genre?) {
        genre?.let {
            genreSelected = it
        }
        view.updateUI(movies, genres, genreSelected)
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
    }
}