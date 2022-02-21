package com.fiz.testsequenia.vp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fiz.testsequenia.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val main= MainPresenter()
        main.loadDataMovies()
    }
}