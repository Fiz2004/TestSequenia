package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.repositories.MoviesRepository
import com.fiz.testsequenia.vp.models.DataItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesContract.View {

    private var state: Parcelable? = null

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private val moviesPresenter: MoviesContract.Presenter by lazy {
        val textGenre = resources.getString(R.string.genres)
        val textMovie = resources.getString(R.string.movies)
        MoviesPresenter(textGenre, textMovie, this, moviesRepository)
    }

    private val adapter: MoviesAdapter by lazy {
        MoviesAdapter(
            { id -> clickMovie(id) },
            { genre -> clickGenre(genre) }
        )
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        state = savedInstanceState?.getParcelable(RECYCLER_VIEW_STATE)

        val genreSelected = savedInstanceState?.getString(KEY_GENRE_SELECTED)
        moviesPresenter.loadGenreSelected(genreSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (state != null)
            binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)

        binding.moviesRecyclerView.layoutManager = GridLayoutManager(activity, 2)

        moviesPresenter.loadMovies()

        binding.repeat.setOnClickListener {
            moviesPresenter.loadMovies()
        }
    }

    override fun setStateLoading(value: Boolean) {
        val visibility = if (value)
            View.VISIBLE
        else
            View.GONE

        binding.circularProgressIndicator.visibility = visibility
        binding.repeat.visibility = View.GONE
    }

    override fun setStateShowMovies(dataItem: List<DataItem>) {
        val state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()

        adapter.submitList(dataItem)

        val manager = binding.moviesRecyclerView.layoutManager
        (manager as? GridLayoutManager)?.spanSizeLookup =
            spanSizeLookup(dataItem)

        binding.moviesRecyclerView.visibility = View.VISIBLE
        binding.moviesRecyclerView.layoutManager = manager
        binding.moviesRecyclerView.adapter = adapter

        binding.moviesRecyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    override fun setStateShowLocalMovies(dataItem: List<DataItem>, message: String?) {
        binding.repeat.visibility = View.GONE

        setStateShowMovies(dataItem)

        if (context != null)
            Toast.makeText(
                context,
                message ?: getString(R.string.networkErrorCashEnabled),
                Toast.LENGTH_LONG
            ).show()
    }

    override fun setStateFullError(message: String?) {
        binding.repeat.visibility = View.VISIBLE
        binding.moviesRecyclerView.visibility = View.GONE
        if (context != null)
            Toast.makeText(
                context,
                message ?: getString(R.string.networkErrorCashDisabled),
                Toast.LENGTH_LONG
            ).show()
    }

    private fun spanSizeLookup(dataItem: List<DataItem>) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                moviesPresenter.getSpanSize(dataItem, position)
        }

    override fun clickMovie(id: Int) {
        moviesPresenter.clickMovie(id)
    }

    override fun clickGenre(genre: Genre) {
        moviesPresenter.clickGenre(genre)
    }

    override fun moveMovieDetails(id: Int) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (_binding != null)
            state = binding.moviesRecyclerView.layoutManager?.onSaveInstanceState()
        outState.putParcelable(RECYCLER_VIEW_STATE, state)

        moviesPresenter.getGenreSelectedName()?.let {
            outState.putString(KEY_GENRE_SELECTED, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        moviesPresenter.cleanUp()
    }

    companion object {
        const val KEY_GENRE_SELECTED = "genre"
        const val RECYCLER_VIEW_STATE = "RECYCLER_VIEW_STATE"
    }
}
