package com.example.multimediahub

import android.media.MediaPlayer

object MyMediaPlayer {
    private var instance: MediaPlayer? = null

    var currentIndex: Int = -1

    fun getInstance(): MediaPlayer {
        if (instance == null) {
            instance = MediaPlayer()
        }
        return instance!!
    }
}