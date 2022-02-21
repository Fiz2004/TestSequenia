package com.fiz.testsequenia.vp.movieDetails

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMovieDetailsBinding

class MovieDetailsFragment : Fragment(), IMovieDetailsView {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private var movieDetailsPresenter: MovieDetailsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieDetailsPresenter = MovieDetailsPresenter(this)
//        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        val args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        movieDetailsPresenter?.setId(args.id)
        movieDetailsPresenter?.onCreateView()

        binding.topAppBar.setNavigationOnClickListener {
            movieDetailsPresenter?.clickBack()
        }

        return binding.root
    }

    override fun onSetImage(url: String?) {
        val imgUri = url?.toUri()?.buildUpon()?.scheme("https")?.build()
        url?.let {
            Glide.with(binding.imageUrl.context)
                .load(imgUri)
                .placeholder(R.drawable.ic_baseline_cloud_download_24)
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(binding.imageUrl)
        }
        if (url == null) {
            Glide.with(binding.imageUrl.context)
                .load(R.drawable.ic_baseline_broken_image_24)
                .into(binding.imageUrl)
        }
    }

    override fun onSetDescription(description: String) {
        binding.descriptionTextView.text = description
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
        binding.topAppBar.title = localizedName
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        movieDetailsPresenter = null
    }

}