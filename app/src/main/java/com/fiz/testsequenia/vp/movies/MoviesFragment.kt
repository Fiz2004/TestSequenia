package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.*
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

    @Inject
    lateinit var moviesRepository: MoviesRepository

    private val moviesPresenter: MoviesContract.Presenter by lazy {
        val textGenre = resources.getString(R.string.genres)
        val textMovie = resources.getString(R.string.movies)
        MoviesPresenter(this, moviesRepository, textGenre = textGenre, textMovie = textMovie)
    }

    private val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(
            { id -> clickMovie(id) },
            { genre -> clickGenre(genre) }
        )
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val genreSelected = savedInstanceState?.getString(KEY_GENRE_SELECTED)
        moviesPresenter.loadGenreSelected(genreSelected)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh -> {
                moviesPresenter.loadMovies(fetchFromRemote = true)
                true
            }
            else -> false
        }
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
        binding.moviesRecyclerView.layoutManager = GridLayoutManager(activity, 2)
        moviesPresenter.loadMovies()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.refresh)
        item.isVisible = moviesPresenter.refreshItemVisible
        super.onPrepareOptionsMenu(menu)
    }

    override fun setStateLoading(value: Boolean) {
        val visibility = if (value)
            View.VISIBLE
        else
            View.GONE

        binding.circularProgressIndicator.visibility = visibility
    }

    override fun setStateShowMovies(dataItem: List<DataItem>) {
        requireActivity().invalidateOptionsMenu()

        moviesAdapter.submitList(dataItem)

        binding.moviesRecyclerView.apply {
            layoutManager?.let {
                val state = it.onSaveInstanceState()
                (it as? GridLayoutManager)?.spanSizeLookup = spanSizeLookup(dataItem)
                it.onRestoreInstanceState(state)
            }
            adapter = moviesAdapter
            visibility = View.VISIBLE
        }
    }

    override fun setStateShowLocalMovies(dataItem: List<DataItem>, message: String?) {
        setStateShowMovies(dataItem)

        context?.let {
            Toast.makeText(
                it,
                getString(R.string.networkErrorCashEnabled),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun setStateFullError(message: String?) {
        requireActivity().invalidateOptionsMenu()

        binding.moviesRecyclerView.visibility = View.GONE
        context?.let {
            Toast.makeText(
                it,
                getString(R.string.networkErrorCashDisabled),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun spanSizeLookup(dataItem: List<DataItem>) =
        object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) =
                moviesPresenter.getSpanSize(dataItem, position)
        }

    private fun clickMovie(id: Int) {
        moviesPresenter.clickMovieCard(id)
    }

    private fun clickGenre(genre: Genre) {
        moviesPresenter.clickGenreButton(genre)
    }

    override fun navigateToMovieDetails(id: Int) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(id)
        findNavController().navigate(action)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
    }
}
