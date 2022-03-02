package com.fiz.testsequenia.vp.movies

import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.model.DataMovies

interface IMoviesView {
    fun onSetTopAppBarTitle(textID: Int)
    fun clickMovie(id: Int)
    fun setManagerAdapter(manager: GridLayoutManager)
    fun setAdapter(adapter: MoviesAdapter)
    fun restoreInstanceState()
    fun saveInstanceState()
    fun initUI()
    fun updateUI(
        dataMovies: DataMovies
    )
}