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

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    override var genreSelected: Genre? = null
        set(value) {
            field = if (value == genreSelected) {
                null
            } else {
                value
            }
        }

    override fun loadGenreSelected(genreSelected: String?) {
        val genreName = genreSelected ?: return
        val genre = Genre(name = genreName)
        this.genreSelected = genre
    }

    override fun getGenreSelected(): String? {
        return genreSelected?.name
    }

    override fun loadMovies() {
        scope.launch {
            view.setLoadingIndicator(active = true)

            when (val resultLoad = moviesRepository.loadData()) {
                is Resource.Success,
                is Resource.SuccessOnlyLocal -> {
                    setupData(resultLoad.data)
                    view.setLoadingIndicator(active = false)
                    view.updateUI(this@MoviesPresenter.movies, genres, genreSelected)
                    if (resultLoad is Resource.SuccessOnlyLocal)
                        view.showError(
                            resultLoad.message ?: "Network request failed, Сashed  data loaded"
                        )
                }
                else -> {
                    view.showFullError(resultLoad.message ?: "Network request failed")
                }
            }
        }
    }

    private fun setupData(data: List<Movie>?) {
        val movies = data ?: listOf()
        this@MoviesPresenter.movies = movies.sortedBy { it.localizedName }
        this@MoviesPresenter.genres =
            movies.flatMap { movie -> movie.genres.map { it } }
                .distinct().filterNot { it == "" }.map { it.toGenre() }
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
}