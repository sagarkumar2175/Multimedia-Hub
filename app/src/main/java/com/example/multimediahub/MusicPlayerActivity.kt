package com.example.multimediahub

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {
    private var currentSong: AudioModal? = null
    private var songsList: ArrayList<AudioModal>? = null
    private var mediaPlayer: MediaPlayer? = null
    private var x = 0f

    // Moved currentTimeTv, totalTimeTv, seekBar, pausePlay, nextBtn, previousBtn, and musicIcon
    // to class-level properties to avoid redundant findViewById calls
    private lateinit var currentTimeTv: TextView
    private lateinit var totalTimeTv: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var pausePlay: ImageButton
    private lateinit var nextBtn: ImageButton
    private lateinit var previousBtn: ImageButton
    private lateinit var musicIcon: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Moved initialization of UI elements here to avoid redundant findViewById calls
        currentTimeTv = findViewById(R.id.current_time)
        totalTimeTv = findViewById(R.id.total_time)
        seekBar = findViewById(R.id.seek_bar)
        pausePlay = findViewById(R.id.pause_play)
        nextBtn = findViewById(R.id.next)
        previousBtn = findViewById(R.id.previous)
        musicIcon = findViewById(R.id.music_icon_big)

        songsList = intent.getSerializableExtra("LIST") as ArrayList<AudioModal>?

        setResourcesWithMusic()

        // Using apply to avoid redundant mediaPlayer? calls
        mediaPlayer?.apply {
            this@MusicPlayerActivity.runOnUiThread(object : Runnable {
                override fun run() {
                    if (isPlaying) {
                        pausePlay.setImageResource(R.drawable.pause)
                        musicIcon.rotation = x++
                    } else {
                        pausePlay.setImageResource(R.drawable.play)
                        musicIcon.rotation = 0f
                    }

                    seekBar.progress = currentPosition
                    currentTimeTv.text = convertToMMSS(currentPosition.toString())

                    Handler().postDelayed(this, 100)
                }
            })
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer!!.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        pausePlay.setOnClickListener { pausePlay() }
        nextBtn.setOnClickListener { playNextSong() }
        previousBtn.setOnClickListener { playPreviousSong() }
    }

    private fun setResourcesWithMusic() {
        currentSong = songsList?.get(MyMediaPlayer.currentIndex)

        if (currentSong == null) {
            Log.d("MusicPlayerActivity", "Error in song resource")
            return
        }

        // ... (update other views with currentSong info)

        playMusic()
    }

    private fun playMusic() {
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(currentSong?.path)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        seekBar.max = mediaPlayer?.duration ?: 0
        seekBar.progress = 0
    }

    private fun playNextSong() {
        if (MyMediaPlayer.currentIndex == songsList?.size?.minus(1))
            return
        MyMediaPlayer.currentIndex += 1
        mediaPlayer?.reset()
        setResourcesWithMusic()
    }

    private fun playPreviousSong() {
        if (MyMediaPlayer.currentIndex == 0)
            return
        MyMediaPlayer.currentIndex -= 1
        mediaPlayer?.reset()
        setResourcesWithMusic()
    }

    private fun pausePlay() {
        mediaPlayer?.let {
            if (it.isPlaying)
                it.pause()
            else
                it.start()
        }
    }

    companion object {
        fun convertToMMSS(duration: String): String {
            val millis = duration.toLong()
            return String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1)
            )
        }
    }
}
