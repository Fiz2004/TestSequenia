package com.fiz.testsequenia.app

import android.app.Application
import com.fiz.testsequenia.model.MoviesRepository
import com.fiz.testsequenia.model.network.MoviesApi

class app:Application() {
    override fun onCreate() {
        super.onCreate()
        MoviesRepository.initialize(MoviesApi())
    }
}