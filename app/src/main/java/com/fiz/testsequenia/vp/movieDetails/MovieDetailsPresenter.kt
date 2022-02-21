package com.fiz.testsequenia.vp.movieDetails

import androidx.navigation.fragment.findNavController
import com.fiz.testsequenia.model.MoviesRepository

class MovieDetailsPresenter(private val view: IMovieDetailsView) {
    private var id: Int = 0
    private val moviesRepository: MoviesRepository = MoviesRepository.get()

    fun onCreateView() {
        val movie = moviesRepository.getSortMovies()?.first { id == it.id }

        movie?.name?.let { view.onSetName(it) }
        movie?.year?.let { view.onSetYear(it) }
        movie?.rating?.let { view.onSetRating(it) } ?: view.onSetRating(null)
        movie?.description?.let { view.onSetDescription(it) }
        movie?.localizedName?.let { view.onSetLocalizedName(it) }

        view.onSetImage(movie?.imageUrl)
    }

    fun clickBack() {
        (view as MovieDetailsFragment).findNavController()
            .popBackStack()
    }

    fun setId(id: Int) {
        this.id = id
    }
}