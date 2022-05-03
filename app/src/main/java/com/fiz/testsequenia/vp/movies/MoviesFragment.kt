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
import com.fiz.testsequenia.app.App
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

class MoviesFragment : Fragment(), MoviesContract.View {

    private val moviesRepository by lazy {
        (requireActivity().application as App).appContainer.moviesRepository
    }

    private val moviesPresenter: MoviesPresenter by lazy {
        MoviesPresenter(this, moviesRepository)
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(
            requireContext(),
            moviesPresenter::clickMovie,
            moviesPresenter::clickGenre
        )
    }

    private lateinit var binding: FragmentMoviesBinding

    private var state: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = savedInstanceState?.getParcelable(RECYCLER_VIEW_STATE)

        val genreSelected = savedInstanceState?.getString(MoviesPresenter.KEY_GENRE_SELECTED)
        if (genreSelected != null)
            moviesPresenter.setGenreSelected1(Genre(name = genreSelected))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.circularProgressIndicator.visibility = View.GONE
        binding.repeat.visibility = View.VISIBLE
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

    override fun moveMovieDetails(id: Int) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::binding.isInitialized)
            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE, state)
        moviesPresenter.onSaveInstanceState(outState)
    }

    companion object {
        const val RECYCLER_VIEW_STATE = "RECYCLER_VIEW_STATE"
    }
}
