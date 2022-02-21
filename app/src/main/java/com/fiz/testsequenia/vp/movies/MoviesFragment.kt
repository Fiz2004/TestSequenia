package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.network.models.MovieProperty
import com.fiz.testsequenia.model.network.models.MoviesProperty
import java.util.concurrent.Executors

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        initUI(null)

//        binding.playButton.setOnClickListener { view: View ->
//            view.findNavController()
//                .navigate(
//                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment("1")
//                )
//        }


        moviesPresenter = MoviesPresenter(this)

        return binding.root
    }

    private fun initUI(listResult: MoviesProperty?) {
        if (listResult == null) return

        val allGenres:MutableSet<String> = mutableSetOf()

        listResult.films.map{ movie -> movie.genres.forEach { allGenres.add(it) }}

        val movies = listResult.films

        adapter = MoviesAdapter(allGenres.distinct(),movies) { position: Int ->
            Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
//            sleepTrackerViewModel.onSleepNightClicked(nightId)
        }
        adapter.addHeaderAndSubmitList()

        val manager = GridLayoutManager(activity, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = when (position) {
                0, allGenres.size + 1 -> 2
                in 1..allGenres.size -> 2
                else -> 1
            }
        }
            binding.moviesRecyclerView.post {
                binding.moviesRecyclerView.layoutManager = manager
                binding.moviesRecyclerView.adapter = adapter
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        moviesPresenter?.destroy()
        moviesPresenter = null
    }

    override fun showMovies(listResult: MoviesProperty) {
        initUI(listResult)
    }
}
