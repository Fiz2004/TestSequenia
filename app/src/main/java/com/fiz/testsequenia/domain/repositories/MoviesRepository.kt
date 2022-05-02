package com.fiz.testsequenia.domain.repositories

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

interface MoviesRepository {
    val genres: List<Genre>?
    val movies: List<Movie>?

    suspend fun loadData(callBack: (List<Movie>, List<Genre>) -> Unit)
}