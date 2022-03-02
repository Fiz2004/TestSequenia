package com.fiz.testsequenia.vp.movies

import android.content.Context
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
import com.fiz.testsequenia.model.DataMovies
import com.fiz.testsequenia.model.MoviesRepository

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null

    private lateinit var adapter: MoviesAdapter
    private lateinit var manager: GridLayoutManager

    private var state: Parcelable? = null

    override fun onAttach(context: Context) {
        moviesPresenter =
            MoviesPresenter(this, DataMovies(MoviesRepository.get()))
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genreSelected = savedInstanceState?.getString(MoviesPresenter.KEY_GENRE_SELECTED)

        if (genreSelected != null)
            moviesPresenter?.setGenreSelected(genreSelected)
        moviesPresenter?.onViewCreated()
    }

    override fun initUI() {
        binding.repeat.visibility = View.GONE
        binding.topAppBar.title = resources.getString(R.string.main)
        binding.circularProgressIndicator.visibility = View.VISIBLE

        binding.repeat.setOnClickListener {
            binding.repeat.visibility = View.GONE
            MoviesRepository.get().loadDataMovies { moviesPresenter?.updateUI() }
        }

        moviesPresenter?.let {
            adapter = MoviesAdapter(
                requireContext(),
                moviesPresenter!!::clickMovie,
                moviesPresenter!!::clickGenre
            )
        }

        manager = GridLayoutManager(activity, 2)
    }

    override fun updateUI(
        dataMovies: DataMovies
    ) {
        if (MoviesRepository.get().message == "") {

            binding.circularProgressIndicator.visibility = View.GONE
            binding.repeat.visibility = View.GONE

            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
            adapter.refreshData(dataMovies)
            manager.spanSizeLookup = spanSizeLookup(dataMovies.genres!!)

            binding.moviesRecyclerView.layoutManager = manager
            binding.moviesRecyclerView.adapter = adapter
            binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)
        } else {
            binding.repeat.visibility = View.VISIBLE
            Toast.makeText(context, MoviesRepository.get().message, Toast.LENGTH_LONG).show()
        }
    }

    private fun spanSizeLookup(genres: List<String>) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, genres.size + 1 -> 2
                in 1..genres.size -> 2
                else -> 1
            }
        }

    override fun clickMovie(id: Int) {
        findNavController().navigate(
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesPresenter?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        moviesPresenter = null
    }

}
