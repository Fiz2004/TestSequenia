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
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.MoviesRepository

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter =
        MoviesPresenter(this, MoviesRepository.get())

    private lateinit var adapter: MoviesAdapter
    private lateinit var manager: GridLayoutManager

    private var state: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val state: Parcelable? = savedInstanceState?.getParcelable("state")
        if (state != null)
            this.state = state

        val genreSelected = savedInstanceState?.getString(MoviesPresenter.KEY_GENRE_SELECTED)
        if (genreSelected != null)
            moviesPresenter.setGenreSelected(genreSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.title = resources.getString(R.string.main)

        binding.repeat.setOnClickListener {
            moviesPresenter.loadData()
        }

        adapter = MoviesAdapter(
            requireContext(),
            moviesPresenter::clickMovie,
            moviesPresenter::clickGenre
        )

        manager = GridLayoutManager(activity, 2)

        moviesPresenter.loadData()
    }

    override fun updateUI(
        moviesWithGenresWithSelected: MoviesWithGenresWithSelected
    ) {
        if (state == null)
            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        adapter.refreshData(moviesWithGenresWithSelected)
        manager.spanSizeLookup = spanSizeLookup(moviesWithGenresWithSelected.genres.size)

        binding.moviesRecyclerView.layoutManager = manager
        binding.moviesRecyclerView.adapter = adapter
        binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)
        if (moviesWithGenresWithSelected.genres.isNotEmpty() || moviesWithGenresWithSelected.movies.isNotEmpty())
            state = null
    }

    override fun showLoadView() {
        binding.circularProgressIndicator.visibility = View.VISIBLE
        binding.repeat.visibility = View.GONE
    }

    override fun hideLoadView() {
        binding.circularProgressIndicator.visibility = View.GONE
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
        state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        findNavController().navigate(
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (state == null)
            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable("state", state)
        moviesPresenter.onSaveInstanceState(outState)
    }
}
