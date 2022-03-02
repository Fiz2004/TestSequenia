package com.fiz.testsequenia

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.fiz.testsequenia.vp.movies.MoviesFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.fiz.testsequenia", appContext.packageName)
    }

    @Test
    fun navigate_to_nav_component() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        val moviesListScenario = launchFragmentInContainer<MoviesFragment>(
            themeResId =
            R.style.Theme_TestSequenia
        )

        moviesListScenario.onFragment { fragment ->

            navController.setGraph(R.navigation.navigation)

            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        onView(withId(R.id.moviesRecyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(16, click())
            )

        assertEquals(navController.currentDestination?.id, R.id.movieDetailsFragment)
    }
}