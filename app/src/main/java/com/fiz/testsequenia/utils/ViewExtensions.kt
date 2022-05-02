package com.fiz.testsequenia.utils

import android.widget.ImageView
import androidx.core.net.toUri
import coil.load
import com.fiz.testsequenia.R

fun ImageView.loadUrl(url: String?) {
    url?.let {
        val imgUri = it.toUri().buildUpon().scheme("https")?.build()
        this.load(imgUri) {
            placeholder(R.drawable.ic_baseline_cloud_download_24)
            error(R.drawable.ic_baseline_broken_image_24)
        }
    } ?: this.load(R.drawable.ic_baseline_broken_image_24)
}