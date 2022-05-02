package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiz.testsequenia.R
import com.fiz.testsequenia.app.App
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding
import com.fiz.testsequenia.utils.loadUrl

class MovieDetailsFragment : Fragment(), MovieDetailsContract.View {

    private val moviesRepository by lazy {
        (requireActivity().application as App).appContainer.moviesRepository
    }

    private val movieDetailsPresenter: MovieDetailsPresenter by lazy {
        MovieDetailsPresenter(this, moviesRepository)
    }

    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        movieDetailsPresenter.onCreateView(args.id)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsPresenter.onViewCreated()
    }

    override fun onSetImage(url: String?) {
        binding.imageUrl.loadUrl(url)

    }

    override fun onClickBack() {
        findNavController().popBackStack()
    }

    override fun onSetDescription(description: String?) {
        binding.descriptionTextView.text = description ?: ""
    }

    override fun onSetRating(rating: Double?) {
        binding.ratingTextView.text = if (rating != null)
            resources.getString(R.string.rating, rating)
        else
            ""
    }

    override fun onSetYear(year: Int) {
        binding.yearTextView.text = resources.getString(R.string.year, year)
    }

    override fun onSetName(name: String) {
        binding.nameTextView.text = name
    }

    override fun onSetLocalizedName(localizedName: String) {
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.appBarLayout).title =
            localizedName
    }
}