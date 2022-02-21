package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null
    private lateinit var adapter: MoviesAdapter
    private lateinit var genres:List<String>
    private lateinit var sortMovies:List<MovieProperty>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        moviesPresenter = MoviesPresenter(this)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initUI(genres: List<String>, movies: List<MovieProperty>, position: Int? = null) {

        adapter = MoviesAdapter(genres, movies, position) { position: Int ->
            if (position <= genres.size) {
                val currentposition = position - 1
                val genreSelected = genres[currentposition]
                val filterMovies = sortMovies.filter { it.genres.contains(genreSelected) }
                initUI(genres, filterMovies, currentposition)
            } else {
                val currentposition=position-genres.size-2
                this@MoviesFragment.findNavController()
                    .navigate(
                        MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(movies[currentposition].id)
                    )
            }
        }
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

    //TODO Когда возвращаемся
    override fun onStart() {
        super.onStart()
        if (this::genres.isInitialized&&this::sortMovies.isInitialized)
        initUI(genres,sortMovies)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        moviesPresenter?.destroy()
        moviesPresenter = null
    }

    override fun showMovies(listResult: MoviesProperty) {
        val allGenres:MutableSet<String> = mutableSetOf()

        listResult.films.map{ movie -> movie.genres.forEach { allGenres.add(it) }}

        genres=allGenres.distinct()

        val movies = listResult.films
        sortMovies=movies.sortedBy { it.localizedName }

        initUI(genres,sortMovies)
    }
}
