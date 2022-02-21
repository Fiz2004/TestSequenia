package com.fiz.testsequenia.app

import android.app.Application
import com.fiz.testsequenia.model.MoviesRepository

class app:Application() {
    override fun onCreate() {
        super.onCreate()
        MoviesRepository.initialize(this)
    }
}