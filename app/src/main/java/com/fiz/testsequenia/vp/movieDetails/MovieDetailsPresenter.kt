package com.fiz.testsequenia.vp.movieDetails

import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository

class MovieDetailsPresenter(
    private val view: MovieDetailsContract.View,
    private val moviesRepository: MoviesRepository,
) : MovieDetailsContract.Presenter {
    private var movie: Movie? = null

    override fun start(id: Int) {
        movie = moviesRepository.movies?.first { id == it.id }
    }


    override fun load() {
        movie?.let {
            view.updateUI(
                name = it.name,
                year = it.year,
                rating = it.rating,
                description = it.description,
                localizedName = it.localizedName,
                url = it.imageUrl
            )
        }
    }
}