package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.data.data_sources.remote.dto.toGenre
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.fiz.testsequenia.utils.Resource
import com.fiz.testsequenia.vp.models.DataItem
import kotlinx.coroutines.*

class MoviesPresenter(
    private val view: MoviesContract.View,
    private val moviesRepository: MoviesRepository,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.Main),
    private val textGenre: String = "",
    private val textMovie: String = "",
) : MoviesContract.Presenter {

    private var genres: List<Genre> = listOf()
    private var movies: List<Movie> = listOf()

    var genreSelected: Genre? = null
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

    override fun getGenreSelectedName(): String? {
        return genreSelected?.name
    }

    override fun loadMovies() {
        scope.launch {
            view.setStateLoading(value = true)
            val resultLoad = moviesRepository.loadData()
            resultLoad.data?.let { setupData(it) }
            view.setStateLoading(value = false)

            when (resultLoad) {
                is Resource.Success -> view.setStateShowMovies(
                    getListDataItem(
                        this@MoviesPresenter.movies,
                        genres,
                        genreSelected
                    )
                )

                is Resource.SuccessOnlyLocal -> view.setStateShowLocalMovies(
                    getListDataItem(
                        this@MoviesPresenter.movies,
                        genres,
                        genreSelected
                    ),
                    resultLoad.message
                )

                is Resource.Error -> view.setStateFullError(
                    resultLoad.message
                )
            }
        }
    }

    private fun getListDataItem(
        movies: List<Movie>,
        genres: List<Genre>,
        genreSelected: Genre?
    ): List<DataItem> {
        return DataItem.getDataItemFromDomain(
            textGenre,
            textMovie,
            movies,
            genres,
            genreSelected
        )
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
        view.setStateShowMovies(getListDataItem(movies, genres, genreSelected))
    }

    override fun getSpanSize(dataItem: List<DataItem>, position: Int): Int {
        val countMovies = dataItem.filterIsInstance<DataItem.MovieItem>().size
        val countTwoSize = dataItem.size - countMovies
        return when (position) {
            in 0 until countTwoSize -> 2
            else -> 1
        }
    }
}