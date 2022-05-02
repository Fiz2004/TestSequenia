package com.fiz.testsequenia.vp.movieDetails

interface MovieDetailsContract {

    interface View {
        fun onSetName(name: String)
        fun onSetYear(year: Int)
        fun onSetRating(rating: Double?)
        fun onSetDescription(description: String?)
        fun onSetLocalizedName(localizedName: String)
        fun onSetImage(url: String?)
        fun onClickBack()
    }

    interface Presenter {

        fun onCreateView(id: Int)

        fun onViewCreated()
    }

}