package com.fiz.testsequenia.vp.movieDetails

interface IMovieDetailsView {
    fun onSetName(name: String)
    fun onSetYear(year: Int)
    fun onSetRating(rating: Double?)
    fun onSetDescription(description: String)
    fun onSetLocalizedName(localizedName: String)
    fun onSetImage(url: String?)
}