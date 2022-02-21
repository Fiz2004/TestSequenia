package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.network.models.MovieProperty

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null

    private lateinit var adapter: MoviesAdapter
    private lateinit var manager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val genreSelected = savedInstanceState?.getString(MoviesPresenter.KEY_GENRE_SELECTED)
        moviesPresenter = MoviesPresenter(this, genreSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        moviesPresenter?.onCreateView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        moviesPresenter?.onStart()
    }

    override fun updateUI(genres: List<String>, sortMovies: List<MovieProperty>, genreSelected: String?) {
        if (!this::adapter.isInitialized) return
        adapter.refreshData(
            genres,
            sortMovies,
            genreSelected
        )
        manager.spanSizeLookup = moviesPresenter?.spanSizeLookup(genres)

        binding.moviesRecyclerView.post {
            binding.moviesRecyclerView.layoutManager = manager
            binding.moviesRecyclerView.adapter = adapter
        }
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

    override fun onDestroy() {
        super.onDestroy()
        moviesPresenter = null
    }

    override fun initUI() {
        binding.topAppBar.title = "Главная"

        if (moviesPresenter == null) return
        adapter = MoviesAdapter(
            requireContext().applicationContext,
            moviesPresenter!!::clickMovie,
            moviesPresenter!!::clickGenre
        )

        manager = GridLayoutManager(activity, 2)
    }

}
