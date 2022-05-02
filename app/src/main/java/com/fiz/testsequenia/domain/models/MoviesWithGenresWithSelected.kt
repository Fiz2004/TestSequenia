package com.fiz.testsequenia.domain.models

data class MoviesWithGenresWithSelected(
    val genres: List<Genre> = listOf(),
    private val sortMovies: List<Movie> = listOf(),
    val genreSelected: Genre? = null
) {
    val movies
        get() = if (isGenreSelected())
            getFilterMovies()
        else
            sortMovies

    private fun getFilterMovies() = sortMovies.filter {
        it.genres.contains(genreSelected?.name)
    }

    fun isGenreSelected(): Boolean {
        return (genreSelected != null)
    }

    fun setGenreSelected(genre: Genre?): MoviesWithGenresWithSelected {
        val genreSelected = if (genreSelected == genre) {
            null
        } else {
            genre
        }
        return copy(genreSelected = genreSelected)
    }

    fun loadData(moviesWithGenresWithSelected: MoviesWithGenresWithSelected): MoviesWithGenresWithSelected {
        return copy(
            genres = moviesWithGenresWithSelected.genres,
            sortMovies = moviesWithGenresWithSelected.sortMovies
        )
    }
}

data class Movie(
    val id: Int,
    val localizedName: String,
    val name: String,
    val year: Int,
    val rating: Double?,
    val imageUrl: String?,
    val description: String?,
    val genres: List<String>
)

data class Genre(
    val name: String
)