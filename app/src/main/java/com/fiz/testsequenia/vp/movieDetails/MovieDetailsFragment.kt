package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment(), IMovieDetailsView {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var movieDetailsPresenter: MovieDetailsPresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())

        movieDetailsPresenter = MovieDetailsPresenter(this)
        val movie = movieDetailsPresenter!!.moviesRepository.listResult?.films?.first { args.id == it.id }

        binding.localizedNameTextView.text = movie?.name
        binding.yearTextView.text = resources.getString(R.string.year, movie?.year)
        if (movie?.rating != null)
            binding.ratingTextView.text = resources.getString(R.string.rating, movie.rating)
        else
            binding.ratingTextView.text = ""
        binding.descriptionTextView.text = movie?.description

        binding.topAppBar.title = movie?.localizedName

        binding.topAppBar.setNavigationOnClickListener {
            this@MovieDetailsFragment.findNavController()
                .popBackStack()
        }

        val imgUri = movie?.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        movie?.imageUrl?.let {
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