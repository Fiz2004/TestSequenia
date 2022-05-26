package com.fiz.testsequenia.vp.movies

import com.fiz.testsequenia.data.repositories.MoviesRepositoryImpl
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.utils.Resource
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

class MoviesPresenterTest {

    private lateinit var view: MoviesContract.View
    private lateinit var moviesRepository: MoviesRepositoryImpl
    private lateinit var moviesPresenter: MoviesPresenter

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineScope =
        TestScope(testCoroutineDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        view = mock()
        moviesRepository = mock()

        Dispatchers.setMain(mainThreadSurrogate)

        moviesPresenter = MoviesPresenter(view, moviesRepository, testCoroutineScope)
    }

    @Test
    fun getGenreSelected() {
    }

    @Test
    fun setGenreSelected() {
    }

    @Test
    fun loadGenreSelected() {
    }

    @Test
    fun testGetGenreSelected() {
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoadMoviesStarted_shouldShowLoadingIndicator() {

        runTest {
            launch(Dispatchers.Main) {
                moviesPresenter.loadMovies()

                verify(view, times(1)).setStateLoading(true)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoadMoviesReturnListMovies_shouldHideLoadingIndicator() {

        moviesRepository.stub {
            onBlocking { loadData() }.doReturn(Resource.Success(listOf<Movie>()))
        }

        runTest {
            launch(Dispatchers.Main) {
                moviesPresenter.loadMovies()

                inOrder(view) {
                    verify(view, times(1)).setStateLoading(true)
                    verify(view, times(1)).setStateLoading(false)
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoadData_ReturnSuccess_shouldSetStateShowMovies() {

        moviesRepository.stub {
            onBlocking { loadData() }.doReturn(Resource.Success(listOf<Movie>()))
        }

        runTest {
            launch(Dispatchers.Main) {
                moviesPresenter.loadMovies()

                verify(view, times(1)).setStateShowMovies(any(), any(), anyOrNull())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoadData_ReturnSuccessLocal_shouldSetStateShowMovies() {

        moviesRepository.stub {
            onBlocking { loadData() }.doReturn(Resource.SuccessOnlyLocal(listOf<Movie>()))
        }

        runTest {
            launch(Dispatchers.Main) {
                moviesPresenter.loadMovies()

                verify(view, times(1)).setStateShowLocalMovies(
                    any(),
                    any(),
                    anyOrNull(),
                    anyOrNull()
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun whenLoadData_ReturnError_shouldSetStateFullError() {

        moviesRepository.stub {
            onBlocking { loadData() }.doReturn(Resource.Error("network"))
        }

        runTest {
            launch(Dispatchers.Main) {
                moviesPresenter.loadMovies()

                verify(view, times(1)).setStateFullError("network")
            }
        }
    }

    @Test
    fun cleanUp() {
    }

    @Test
    fun clickMovie() {
    }

    @Test
    fun clickGenre() {
    }
}