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
    private lateinit var adapter: MoviesAdapter
    private var position:Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState!=null){
            position = savedInstanceState.getInt("position")
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

        if (moviesPresenter==null)
            moviesPresenter = MoviesPresenter(this,position)
    }

    private fun updateUI(genres: List<String>, movies: List<MovieProperty>, position: Int? = null) {

        val call: (Int) -> Unit = callBackClick(genres, movies)
        val call1: (Int, String) -> Unit = callBackClick1(genres, movies)
        adapter = MoviesAdapter(genres, movies, position, call, call1)
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

    private fun callBackClick(
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

    private fun callBackClick1(
        genres: List<String>,
        movies: List<MovieProperty>
    ): (Int, String) -> Unit {
        return fun(position: Int, genre: String) {
            moviesPresenter?.clickGenre(position - 1, genre)
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

    override fun showMovies(genres: List<String>, sortMovies: List<MovieProperty>, currentPosition: Int?) {
        updateUI(genres, sortMovies, currentPosition)
    }
}
