package com.fiz.testsequenia.data.data_sources.remote.dto

import com.squareup.moshi.Json

data class MovieDto(
    val id: Int,
    @Json(name = "localized_name") val localizedName: String,
    val name: String,
    val year: Int,
    val rating: Double?,
    @Json(name = "image_url") val imageUrl: String?,
    val description: String?,
    val genres: List<String>
)