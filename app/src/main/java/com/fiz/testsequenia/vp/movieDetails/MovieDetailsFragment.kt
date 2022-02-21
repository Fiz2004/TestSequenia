package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding
import com.fiz.testsequenia.vp.movies.MoviesPresenter

class MovieDetailsFragment : Fragment(),IMovieDetailsView {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var movieDetailsPresenter: MovieDetailsPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        val id = "NumCorrect: ${args.id}"

        movieDetailsPresenter = MovieDetailsPresenter(this)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        movieDetailsPresenter?.destroy()
        movieDetailsPresenter = null
    }

}