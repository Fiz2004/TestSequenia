package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.fiz.testsequenia.utils.loadUrl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(), MovieDetailsContract.View {

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private val movieDetailsPresenter: MovieDetailsContract.Presenter by lazy {
        MovieDetailsPresenter(this, moviesRepository)
    }

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        movieDetailsPresenter.start(args.id)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailsPresenter.load()
    }

    override fun updateUI(
        name: String,
        year: Int,
        rating: Double?,
        description: String,
        localizedName: String,
        url: String?
    ) {

        binding.imageUrl.loadUrl(url)

        binding.descriptionTextView.text = description

        binding.ratingTextView.text = if (rating != null)
            resources.getString(R.string.rating, rating)
        else
            ""

        binding.yearTextView.text = resources.getString(R.string.year, year)

        binding.nameTextView.text = name

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.mainToolbar)
        toolbar.title = localizedName
    }

    override fun onClickBack() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}