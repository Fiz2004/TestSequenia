package com.fiz.testsequenia.vp.movieDetails

import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty

class MovieDetailsPresenter(private val view: IMovieDetailsView, private val moviesRepository: MoviesRepository) {
    private var movie: MovieProperty? = null

    fun onCreateView(id: Int) {
        movie = moviesRepository.getSortMovies()?.first { id == it.id }
    }

    fun onViewCreated() {
        movie?.let {
            view.onSetName(it.name)
            view.onSetYear(it.year)
            view.onSetRating(it.rating)
            view.onSetDescription(it.description)
            view.onSetLocalizedName(it.localizedName)
            view.onSetImage(it.imageUrl)
        }
    }

    fun clickBack() {
        view.onClickBack()
    }
}