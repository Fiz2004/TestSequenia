package com.fiz.testsequenia.domain.repositories

import com.fiz.testsequenia.data.repositories.Resource
import com.fiz.testsequenia.domain.models.Movie

interface MoviesRepository {
    val movies: List<Movie>?

    suspend fun loadData(): Resource<List<Movie>?>
}