package com.fiz.testsequenia.vp.movies

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

    override fun initUI() {
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
        dataMovies: DataMovies,
        getSpanSizeLookup: (List<String>) -> GridLayoutManager.SpanSizeLookup,
    ) {
        if (!this::adapter.isInitialized) return

        hideProgressIndicator()

        saveInstanceState()
        adapter.refreshData(dataMovies)
        manager.spanSizeLookup = getSpanSizeLookup(dataMovies.genres!!)

        setManagerAdapter(manager)
        setAdapter(adapter)
        restoreInstanceState()
    }

    private fun hideProgressIndicator() {
        binding.circularProgressIndicator.visibility = View.GONE

    }

    override fun onAttach(context: Context) {
        moviesPresenter =
            MoviesPresenter(this, MoviesRepository.get(), DataMovies(MoviesRepository.get()))
        super.onAttach(context)
    }

    override fun restoreInstanceState() {
        binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)

    }

    override fun saveInstanceState() {
        state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
    }

    override fun setAdapter(adapter: MoviesAdapter) {
        binding.moviesRecyclerView.adapter = adapter

    }

    override fun setManagerAdapter(manager: GridLayoutManager) {
        binding.moviesRecyclerView.layoutManager = manager
    }

    override fun clickMovie(id: Int) {
        findNavController().navigate(
            MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        )
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesPresenter?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesPresenter?.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        moviesPresenter = null
    }

    override fun onSetTopAppBarTitle(textID: Int) {
        binding.topAppBar.title = resources.getString(textID)
    }

}
