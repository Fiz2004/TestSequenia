package com.fiz.testsequenia.domain.repositories

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected

interface MoviesRepository {

    suspend fun loadData(): MoviesWithGenresWithSelected

    fun getGenres(): List<Genre>

    fun getSortMovies(): List<Movie>
}