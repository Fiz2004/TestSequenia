package com.fiz.testsequenia.vp.movies

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fiz.testsequenia.R
import com.fiz.testsequenia.databinding.ListItemGenreBinding
import com.fiz.testsequenia.databinding.ListItemHeaderBinding
import com.fiz.testsequenia.databinding.ListItemMovieBinding
import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_GENRE = 1
private const val ITEM_VIEW_TYPE_MOVIE = 2

class MoviesAdapter(
    private val context: Context,
    private val onClickMovie: ((Int) -> Unit)?,
    private val onClickGenre: ((Genre) -> Unit)?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var genres: List<Genre> = listOf()
    private var movies: List<Movie> = listOf()
    var genreSelected: Genre? = null

    var data: List<DataItem> = listOf()

    fun refreshData(movies: List<Movie>, genres: List<Genre>, genreSelected: Genre?) {
        this.movies = movies
        this.genres = genres
        this.genreSelected = genreSelected

        val newData: List<DataItem> =
            listOf(DataItem.Header(context.resources.getString(R.string.genres))) +
                    genres.map { DataItem.GenreItem(it) } +
                    listOf(
                        DataItem.Header(
                            context.resources.getString(R.string.movies)
                        )
                    ) + (if ((genreSelected != null))
                movies.filter {
                    it.genres.contains(genreSelected.name)
                }
            else
                movies).map {
                DataItem.MoviePropertyItem(it)
            }

        val diffCallback = DataDiffCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        data = newData

        diffResult.dispatchUpdatesTo(this)
    }

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
        return when (data[position]) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.GenreItem -> ITEM_VIEW_TYPE_GENRE
            else -> ITEM_VIEW_TYPE_MOVIE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val header = data[position] as DataItem.Header
                holder.bind(header.title)
            }
            is GenreViewHolder -> {
                val genreItem = data[position] as DataItem.GenreItem
                holder.bind(
                    genreItem.genre,
                    genreSelected,
                    onClickGenre
                )
            }
            is MovieViewHolder -> {
                val moviePropertyItem = data[position] as DataItem.MoviePropertyItem
                holder.bind(moviePropertyItem.movieDto, onClickMovie)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class HeaderViewHolder(private val binding: ListItemHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: String? = null

    fun bind(item: String) {
        this.item = item
        binding.text.text = item
    }
}

class GenreViewHolder(private val binding: ListItemGenreBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: Genre? = null

    fun bind(item: Genre, genreSelected: Genre?, onClickGenre: ((Genre) -> Unit)?) {
        this.item = item
        binding.genreButton.text = item.name
        binding.genreButton.isChecked = genreSelected == item

        binding.genreButton.setOnClickListener {
            if (onClickGenre != null) {
                onClickGenre(item)
            }
        }
    }

}

class MovieViewHolder(private val binding: ListItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var item: Movie? = null

    fun bind(item: Movie, callback: ((Int) -> Unit)?) {
        this.item = item
        val imgUri = item.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
        item.imageUrl?.let {
            binding.imgMovie.load(imgUri) {
                placeholder(R.drawable.ic_baseline_cloud_download_24)
                error(R.drawable.ic_baseline_broken_image_24)
            }
        }
        if (item.imageUrl == null) {
            binding.imgMovie.load(R.drawable.ic_baseline_broken_image_24)
        }
        binding.nameMovie.text = item.localizedName
        binding.root.setOnClickListener {
            if (callback != null) {
                callback(item.id)
            }
        }
    }
}

sealed class DataItem {
    data class GenreItem(val genre: Genre) : DataItem()
    data class MoviePropertyItem(val movieDto: Movie) : DataItem()
    data class Header(val title: String) : DataItem()
}

class DataDiffCallback(private val oldList: List<DataItem>, private val newList: List<DataItem>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition] is DataItem.Header && newList[newItemPosition] is DataItem.Header)
            return (oldList[oldItemPosition] as DataItem.Header).title == (newList[newItemPosition] as DataItem.Header).title
        if (oldList[oldItemPosition] is DataItem.GenreItem && newList[newItemPosition] is DataItem.GenreItem)
            return (oldList[oldItemPosition] as DataItem.GenreItem).genre == (newList[newItemPosition] as DataItem.GenreItem).genre
        if (oldList[oldItemPosition] is DataItem.MoviePropertyItem && newList[newItemPosition] is DataItem.MoviePropertyItem)
            return (oldList[oldItemPosition] as DataItem.MoviePropertyItem).movieDto.id == (newList[newItemPosition] as DataItem.MoviePropertyItem).movieDto.id
        return false
    }

}