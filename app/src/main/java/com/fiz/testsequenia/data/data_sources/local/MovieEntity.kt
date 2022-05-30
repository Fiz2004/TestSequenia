package com.fiz.testsequenia.data.data_sources.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fiz.testsequenia.domain.models.Movie

@Entity
class MovieEntity(
    @PrimaryKey
    val id: Int,
    val localizedName: String,
    val name: String,
    val year: Int,
    val rating: Double?,
    val imageUrl: String?,
    val description: String,
    val genres: List<String>,
)

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        localizedName = localizedName,
        name = name,
        year = year,
        rating = rating,
        imageUrl = imageUrl,
        description = description,
        genres = genres
    )
}

fun Movie.toMovieEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        localizedName = localizedName,
        name = name,
        year = year,
        rating = rating,
        imageUrl = imageUrl,
        description = description,
        genres = genres
    )
}