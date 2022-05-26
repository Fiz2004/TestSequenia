package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesContract.View {

    private var state: Parcelable? = null

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private val moviesPresenter: MoviesContract.Presenter by lazy {
        MoviesPresenter(this, moviesRepository)
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(
            requireContext(),
            { id -> clickMovie(id) },
            { genre -> clickGenre(genre) }
        )
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = savedInstanceState?.getParcelable(RECYCLER_VIEW_STATE)

        val genreSelected = savedInstanceState?.getString(KEY_GENRE_SELECTED)
        moviesPresenter.loadGenreSelected(genreSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        moviesPresenter.cleanUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (state != null)
            binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        moviesPresenter.loadMovies()

        binding.repeat.setOnClickListener {
            moviesPresenter.loadMovies()
        }
    }

    override fun updateUI(
        movies: List<Movie>, genres: List<Genre>, genreSelected: Genre?
    ) {
        val state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()

        adapter.refreshData(movies, genres, genreSelected)

        val manager = binding.moviesRecyclerView.layoutManager
        (manager as? GridLayoutManager)?.spanSizeLookup =
            spanSizeLookup(genres.size)

        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.moviesRecyclerView.layoutManager = manager
        binding.moviesRecyclerView.adapter = adapter

        binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    override fun setLoadingIndicator(active: Boolean) {
        val visibility = if (active)
            View.VISIBLE
        else
            View.GONE

        binding.circularProgressIndicator.visibility = visibility
        binding.repeat.visibility = View.GONE
    }

    override fun showError(message: String) {
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showFullError(message: String) {
        binding.circularProgressIndicator.visibility = View.GONE
        binding.repeat.visibility = View.VISIBLE
        binding.moviesRecyclerView.visibility = View.GONE
        if (context != null)
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun spanSizeLookup(countGenres: Int) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, countGenres + 1 -> 2
                in 1..countGenres -> 2
                else -> 1
            }
        }

    override fun clickMovie(id: Int) {
        moviesPresenter.clickMovie(id)
    }

    override fun clickGenre(genre: Genre) {
        moviesPresenter.clickGenre(genre)
    }

    override fun moveMovieDetails(id: Int) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding != null)
            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE, state)

        moviesPresenter.getGenreSelected()?.let {
            outState.putString(KEY_GENRE_SELECTED, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
        const val RECYCLER_VIEW_STATE = "RECYCLER_VIEW_STATE"
    }
}
