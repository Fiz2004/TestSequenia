package com.fiz.testsequenia.vp.movieDetails

import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailsPresenterTest {

    lateinit var movieDetailsPresenter: MovieDetailsPresenter

    @Mock
    lateinit var view: IMovieDetailsView

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Test
    fun `check onViewCreated`() {
        val sortMovies: List<MovieProperty> = listOf(
            MovieProperty(
                id = 3,
                localizedName = "",
                name = "",
                year = 0,
                rating = 0.0,
                imageUrl = "",
                description = "15",
                genres = listOf("")
            ),
            MovieProperty(
                id = 2,
                localizedName = "",
                name = "",
                year = 0,
                rating = 0.0,
                imageUrl = "",
                description = "23",
                genres = listOf("")
            )
        )
        Mockito.`when`(moviesRepository.getSortMovies()).thenReturn(sortMovies)

        movieDetailsPresenter = MovieDetailsPresenter(view, moviesRepository)
        movieDetailsPresenter.onCreateView(2)
        movieDetailsPresenter.onViewCreated()

        Mockito.verify(view, Mockito.times(1)).onSetDescription("23")

    }
}