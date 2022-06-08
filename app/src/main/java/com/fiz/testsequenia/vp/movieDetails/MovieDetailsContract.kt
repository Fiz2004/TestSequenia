package com.fiz.testsequenia.vp.movieDetails

interface MovieDetailsContract {

    interface View {
        fun updateUI(
            name: String,
            year: Int,
            rating: Double?,
            description: String,
            localizedName: String,
            url: String?
        )

        fun onClickBack()
    }

    interface Presenter {
        fun start(id: Int)
        fun load()
    }

}