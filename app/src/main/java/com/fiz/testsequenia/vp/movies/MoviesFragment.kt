package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.network.models.MoviesProperty

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)


        binding.playButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment("1")
                )
        }

        moviesPresenter = MoviesPresenter(this)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        moviesPresenter?.destroy()
        moviesPresenter = null
    }

    override fun showMovies(listResult: MoviesProperty) {
        listResult
    }
}
