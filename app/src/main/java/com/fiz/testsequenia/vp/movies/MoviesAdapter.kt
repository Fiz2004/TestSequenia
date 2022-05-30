package com.fiz.testsequenia.vp.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fiz.testsequenia.databinding.ListItemGenreBinding
import com.fiz.testsequenia.databinding.ListItemHeaderBinding
import com.fiz.testsequenia.databinding.ListItemMovieBinding
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.utils.loadUrl
import com.fiz.testsequenia.vp.models.DataItem

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_GENRE = 1
private const val ITEM_VIEW_TYPE_MOVIE = 2

class MoviesAdapter(
    private val onClickMovie: (Int) -> Unit,
    private val onClickGenre: (Genre) -> Unit
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(DataItemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(
                ListItemHeaderBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_GENRE -> GenreViewHolder(
                ListItemGenreBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            ITEM_VIEW_TYPE_MOVIE -> MovieViewHolder(
                ListItemMovieBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.HeaderItem -> ITEM_VIEW_TYPE_HEADER
            is DataItem.GenreItem -> ITEM_VIEW_TYPE_GENRE
            is DataItem.MovieItem -> ITEM_VIEW_TYPE_MOVIE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = item as DataItem.HeaderItem
                holder.bind(headerItem)
            }
            is GenreViewHolder -> {
                val genreItem = item as DataItem.GenreItem
                holder.bind(
                    genreItem,
                    onClickGenre
                )
            }
            is MovieViewHolder -> {
                val movieItem = item as DataItem.MovieItem
                holder.bind(movieItem, onClickMovie)
            }
        }
    }
}

class HeaderViewHolder(private val binding: ListItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: DataItem.HeaderItem? = null

    fun bind(item: DataItem.HeaderItem) {
        this.item = item
        binding.text.text = item.title
    }
}

class GenreViewHolder(private val binding: ListItemGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: DataItem.GenreItem? = null

    fun bind(item: DataItem.GenreItem, onClickGenre: ((Genre) -> Unit)?) {
        this.item = item
        binding.genreButton.text = item.genre.name
        binding.genreButton.isChecked = item.selected

        binding.genreButton.setOnClickListener {
            if (onClickGenre != null) {
                onClickGenre(item.genre)
            }
        }
    }

}

class MovieViewHolder(private val binding: ListItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: DataItem.MovieItem? = null

    fun bind(item: DataItem.MovieItem, callback: (Int) -> Unit) {
        this.item = item

        binding.imgMovie.loadUrl(item.movie.imageUrl)

        binding.nameMovie.text = item.movie.localizedName

        binding.root.setOnClickListener {
            callback(item.movie.id)
        }
    }
}

private val DataItemComparator = object : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return when {
            oldItem is DataItem.HeaderItem && newItem is DataItem.HeaderItem ->
                oldItem.title == newItem.title

            oldItem is DataItem.GenreItem && newItem is DataItem.GenreItem ->
                oldItem.genre == newItem.genre

            oldItem is DataItem.MovieItem && newItem is DataItem.MovieItem ->
                oldItem.movie.id == newItem.movie.id

            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}