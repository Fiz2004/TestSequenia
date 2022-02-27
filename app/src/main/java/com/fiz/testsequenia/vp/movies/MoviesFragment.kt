package com.fiz.testsequenia.vp.movies

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.FragmentMoviesBinding
import com.fiz.testsequenia.model.MoviesRepository

class MoviesFragment : Fragment(), IMoviesView {
    private var _binding: FragmentMoviesBinding? = null
    val binding get() = _binding!!

    private var moviesPresenter: MoviesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val genreSelected = savedInstanceState?.getString(MoviesPresenter.KEY_GENRE_SELECTED)
        moviesPresenter = MoviesPresenter(this, genreSelected, MoviesRepository.get())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        moviesPresenter?.onCreateView()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        moviesPresenter?.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        moviesPresenter?.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        moviesPresenter?.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        moviesPresenter = null
    }

    override fun onSetTopAppBarTitle(textID: Int) {
        binding.topAppBar.title = resources.getString(textID)
    }

}
