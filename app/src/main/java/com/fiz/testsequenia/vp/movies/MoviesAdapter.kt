package com.fiz.testsequenia.vp.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fiz.testsequenia.databinding.ListItemGenreBinding
import com.fiz.testsequenia.databinding.ListItemHeaderBinding
import com.fiz.testsequenia.databinding.ListItemMovieBinding
import com.fiz.testsequenia.model.network.models.MovieProperty

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_GENRE = 1
private val ITEM_VIEW_TYPE_MOVIE = 2

class MoviesAdapter(
    private val genre: List<String>,
    private val movies: List<MovieProperty>,
    val callback: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var data: List<DataItem>

    fun addHeaderAndSubmitList() {
        data =
            listOf(DataItem.Header("Жанры")) + genre.map { DataItem.GenreItem(it) } + listOf(DataItem.Header("Фильмы")) + movies.map {
                DataItem.MoviePropertyItem(it)
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder(ListItemHeaderBinding.inflate(inflater, parent, false))
            ITEM_VIEW_TYPE_GENRE -> GenreViewHolder(ListItemGenreBinding.inflate(inflater, parent, false))
            ITEM_VIEW_TYPE_MOVIE -> MovieViewHolder(ListItemMovieBinding.inflate(inflater, parent, false))
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, genre.size + 1 -> ITEM_VIEW_TYPE_HEADER
            in 1..genre.size -> ITEM_VIEW_TYPE_GENRE
            else -> ITEM_VIEW_TYPE_MOVIE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind((data[position] as DataItem.Header).title)
            }
            is GenreViewHolder -> {
                holder.bind((data[position] as DataItem.GenreItem).genre,callback)
            }
            is MovieViewHolder -> {
                holder.bind((data[position] as DataItem.MoviePropertyItem).movieProperty,callback)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class HeaderViewHolder(val binding: ListItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        var item: String? = null

        fun bind(item: String) {
            this.item = item
            binding.text.text = item
        }
    }

    class GenreViewHolder(val binding: ListItemGenreBinding) : RecyclerView.ViewHolder(binding.root) {
        var item: String? = null

        fun bind(item: String,callback: (Int) -> Unit) {
            this.item = item
            binding.genreButton.text = item
            binding.genreButton.setOnClickListener {
                callback(layoutPosition)
            }
        }

    }

    class MovieViewHolder(val binding: ListItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        var item: MovieProperty? = null

        fun bind(item: MovieProperty,callback: (Int) -> Unit) {
            this.item = item
            item.imageUrl
            val imgUri = item.imageUrl?.toUri()?.buildUpon()?.scheme("https")?.build()
            item.imageUrl?.let {
                Glide.with(binding.imgMovie.context)
                    .load(imgUri)
                    .into(binding.imgMovie)
            }
            binding.nameMovie.text = item.name
            binding.root.setOnClickListener { callback(layoutPosition) }
        }
    }
}

sealed class DataItem {
    data class GenreItem(val genre: String) : DataItem()
    data class MoviePropertyItem(val movieProperty: MovieProperty) : DataItem()
    data class Header(val title: String) : DataItem()
}