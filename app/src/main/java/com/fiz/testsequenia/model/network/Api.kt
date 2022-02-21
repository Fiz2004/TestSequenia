package com.fiz.testsequenia.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL =
    "https://s3-eu-west-1.amazonaws.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()



data class MovieProperty(
    val id: Int,
    @Json(name = "localized_name") val localizedName: String,
    val name: String,
    val year: Int,
    val rating: Double?,
    @Json(name = "image_url") val imageUrl: String?,
    val description: String?,
    val genres:List<String>
)

//@JsonClass(generateAdapter = true)
data class MoviesProperty(
    val films:List<MovieProperty>
)


interface MoviesApiService {
    @GET("sequeniatesttask/films.json")
    suspend fun getProperties():
            MoviesProperty
}

object MoviesApi {
    val retrofitService: MoviesApiService by lazy {
        retrofit.create(MoviesApiService::class.java)
    }
}