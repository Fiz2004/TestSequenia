package com.fiz.testsequenia.vp.movieDetails

import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository

class MovieDetailsPresenter(
    private val view: MovieDetailsContract.View,
    private val moviesRepository: MoviesRepository
) : MovieDetailsContract.Presenter {
    private var movie: Movie? = null

    override fun onCreateView(id: Int) {
        movie = moviesRepository.getSortMovies().first { id == it.id }
    }

    override fun onViewCreated() {
        movie?.let {
            view.onSetName(it.name)
            view.onSetYear(it.year)
            view.onSetRating(it.rating)
            view.onSetDescription(it.description)
            view.onSetLocalizedName(it.localizedName)
            view.onSetImage(it.imageUrl)
        }
    }
}