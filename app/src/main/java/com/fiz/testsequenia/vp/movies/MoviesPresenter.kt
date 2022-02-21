package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.model.IPresenter
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MoviesPresenter(val view:IMoviesView): IPresenter {

    val moviesRepository: MoviesRepository = MoviesRepository.get()

    init{
        moviesRepository.addPresenter(this)
    }

    override fun loadMovies(listResult: MoviesProperty){
        view.showMovies(listResult)
    }

    fun destroy() {
    }
}