package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.network.models.MovieProperty

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null
    private var genreSelected: String? = null

    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            genreSelected = savedInstanceState.getString(MoviesPresenter.KEY_GENRE_SELECTED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initUI() {
        binding.topAppBar.title = "Главная"

        if (moviesPresenter == null)
            moviesPresenter = MoviesPresenter(this, genreSelected)
    }

    private fun updateUI(genres: List<String>, movies: List<MovieProperty>, genreSelected: String? = null) {

        adapter = MoviesAdapter(
            genres,
            movies,
            genreSelected,
            requireContext().applicationContext,
            onClickMovie(genres, movies),
            onClickGenre()
        )

        adapter.addHeaderAndSubmitList()

        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, genres.size + 1 -> 2
                in 1..genres.size -> 2
                else -> 1
            }
        }
        binding.moviesRecyclerView.post {
            binding.moviesRecyclerView.layoutManager = manager
            binding.moviesRecyclerView.adapter = adapter
        }
    }

    private fun onClickMovie(
        genres: List<String>,
        movies: List<MovieProperty>
    ): (Int) -> Unit {
        return fun(position: Int) {
            val currentPosition = position - genres.size - 2
            this@MoviesFragment.findNavController()
                .navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movies[currentPosition].id)
                )
        }
    }

    private fun onClickGenre(): (String) -> Unit {
        return fun(genre: String) {
            moviesPresenter?.clickGenre(genre)
        }
    }

    override fun onStart() {
        super.onStart()
        moviesPresenter?.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesPresenter?.onSaveInstanceState(outState)
    }

    override fun onDetach() {
        super.onDetach()
        moviesPresenter?.destroy()
        moviesPresenter = null
    }

    override fun showMovies(genres: List<String>, sortMovies: List<MovieProperty>, genreSelected: String?) {
        updateUI(genres, sortMovies, genreSelected)
    }
}
