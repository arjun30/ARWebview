package com.arjun.arwebview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {

    fun getVideosList(): Array<String> {
        return arrayOf<String>(
            "file:///android_asset/video1.mp4",
            "file:///android_asset/video2.mp4",
            "file:///android_asset/video3.mp4"
        )
    }
}