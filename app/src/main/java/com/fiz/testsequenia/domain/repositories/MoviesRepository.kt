package com.fiz.testsequenia.domain.repositories

import com.fiz.testsequenia.data.data_sources.remote.dto.MovieDto
import com.fiz.testsequenia.domain.models.MoviesWithGenresWithSelected

interface MoviesRepository {

    suspend fun loadData(): MoviesWithGenresWithSelected

    fun getGenres(): List<String>

    fun getSortMovies(): List<MovieDto>
}