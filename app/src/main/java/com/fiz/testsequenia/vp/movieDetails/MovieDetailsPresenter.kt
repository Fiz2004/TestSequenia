package com.fiz.testsequenia.vp.movieDetails

import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MovieDetailsPresenter(val view: IMovieDetailsView) {


    val moviesRepository: MoviesRepository = MoviesRepository.get()

//    init{
//        moviesRepository.addPresenter(this)
//    }
//
//    override fun loadMovies(listResult: MoviesProperty){
//        view.showMovies(listResult)
//    }

    fun destroy() {
    }
}