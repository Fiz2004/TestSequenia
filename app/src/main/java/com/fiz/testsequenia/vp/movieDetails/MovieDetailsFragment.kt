package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment(),IMovieDetailsView {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var movieDetailsPresenter: MovieDetailsPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())

        movieDetailsPresenter = MovieDetailsPresenter(this)
        val movie = movieDetailsPresenter!!.moviesRepository.listResult.films.first { args.id == it.id }

        binding.localizedNameTextView.text = movie.localizedName
        binding.yearTextView.text = movie.year.toString()
        binding.ratingTextView.text = movie.rating.toString()
        binding.descriptionTextView.text = movie.description

        binding.topAppBar.setNavigationOnClickListener {
            this@MovieDetailsFragment.parentFragmentManager.popBackStack()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.new_crime -> {
                    true
                }
                else -> {
                    true
                }
            }
        }

        val imgUri = movie.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        movie.imageUrl?.let {
            Glide.with(binding.imageUrl.context)
                .load(imgUri)
                .into(binding.imageUrl)
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        movieDetailsPresenter?.destroy()
        movieDetailsPresenter = null
    }

}