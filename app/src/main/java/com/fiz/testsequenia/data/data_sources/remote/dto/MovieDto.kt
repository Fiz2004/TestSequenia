package com.fiz.testsequenia.data.data_sources.remote.dto

import com.fiz.testsequenia.domain.models.Genre
import com.fiz.testsequenia.domain.models.Movie
import com.squareup.moshi.Json

data class MovieDto(
    val id: Int?,
    @Json(name = "localized_name") val localizedName: String?,
    val name: String?,
    val year: Int?,
    val rating: Double?,
    @Json(name = "image_url") val imageUrl: String?,
    val description: String?,
    val genres: List<String?>?
)

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id ?: 0,
        localizedName = localizedName ?: "",
        name = name ?: "",
        year = year ?: 0,
        rating = rating,
        imageUrl = imageUrl,
        description = description ?: "",
        genres = genres?.mapNotNull { it } ?: listOf()
    )
}

fun String.toGenre(): Genre {
    return Genre(
        name = this
    )
}