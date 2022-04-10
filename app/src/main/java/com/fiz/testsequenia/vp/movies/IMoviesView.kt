package com.fiz.testsequenia.vp.movies

interface IMoviesView {
    fun moveMovieDetails(id: Int)
    fun updateUI(
        moviesWithGenresWithSelected: MoviesWithGenresWithSelected
    )

    fun showError(message: String)
    fun showLoadView()
    fun hideLoadView()
}