package com.fiz.testsequenia.domain.repositories

import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.utils.Resource

interface MoviesRepository {
    val movies: List<Movie>?

    suspend fun loadData(fetchFromRemote: Boolean = false): Resource<List<Movie>?>
}