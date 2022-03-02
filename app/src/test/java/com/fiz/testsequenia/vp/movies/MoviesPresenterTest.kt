package com.fiz.testsequenia.vp.movies

import android.content.Context
import android.content.res.Resources
import com.fiz.testsequenia.R
import com.fiz.testsequenia.model.DataMovies
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.models.MovieProperty
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MoviesPresenterTest {

    lateinit var moviesPresenter: MoviesPresenter

    @Mock
    private val mockApplicationContext: Context? = null

    @Mock
    private val mockContextResources: Resources? = null

    @Mock
    lateinit var view: IMoviesView

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var moviesRepository: MoviesRepository

    @Test
    fun `onViewCreated if first run`() {

        val sortMovies: List<MovieProperty> = listOf(
            MovieProperty(
                id = 3,
                localizedName = "",
                name = "",
                year = 0,
                rating = 0.0,
                imageUrl = "",
                description = "15",
                genres = listOf("Драма")
            ),
            MovieProperty(
                id = 2,
                localizedName = "",
                name = "",
                year = 0,
                rating = 0.0,
                imageUrl = "",
                description = "23",
                genres = listOf("Комедия")
            )
        )
        val genres = listOf("Драма", "Комедия")
        Mockito.`when`(moviesRepository.getSortMovies()).thenReturn(sortMovies)
        Mockito.`when`(moviesRepository.getGenres()).thenReturn(genres)

        moviesPresenter = MoviesPresenter(view, moviesRepository, DataMovies((moviesRepository)))
        moviesPresenter.onViewCreated()

        Mockito.verify(view, Mockito.times(1)).onSetTopAppBarTitle(R.string.main)
        Mockito.verify(view, Mockito.times(1)).initUI()
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

    @Test
    fun `spanSizeLookup if size=2`() {
        moviesPresenter = MoviesPresenter(view, moviesRepository, DataMovies((moviesRepository)))

        val genres = listOf("Драма", "Комедия")
        val expected = moviesPresenter.spanSizeLookup(genres)

        Assert.assertEquals(expected.getSpanSize(0), 2)
        Assert.assertEquals(expected.getSpanSize(1), 2)
        Assert.assertEquals(expected.getSpanSize(2), 2)
        Assert.assertEquals(expected.getSpanSize(3), 2)
        Assert.assertEquals(expected.getSpanSize(4), 1)

    }
}