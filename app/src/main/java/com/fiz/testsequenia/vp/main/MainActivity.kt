package com.fiz.testsequenia.vp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fiz.testsequenia.R

class MainActivity : AppCompatActivity(),IMainView {
    private var mainPresenter: MainPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainPresenter = MainPresenter(this)
        mainPresenter?.loadDataMovies()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter?.detachView()
        if (isFinishing) {
            mainPresenter?.destroy()
            mainPresenter=null
        }
    }
}