package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.R
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MoviesPresenterTest {

    lateinit var moviesPresenter: MoviesPresenter

    @Mock
    lateinit var view: IMoviesView

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Test
    fun `onCreateView if first run`() {

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


        moviesPresenter = MoviesPresenter(view, null, moviesRepository)
        moviesPresenter.onCreateView()

        Mockito.verify(view, Mockito.times(1)).onSetTopAppBarTitle(R.string.main)
    }

    @Test
    fun onLoadMovies() {
    }

    @Test
    fun onSaveInstanceState() {
    }

    @Test
    fun onStart() {
    }

    @Test
    fun onDestroyView() {
    }
}