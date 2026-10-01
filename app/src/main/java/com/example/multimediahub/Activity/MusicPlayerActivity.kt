package com.example.multimediahub.Activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.multimediahub.Adapter.MusicAdapter
import com.example.multimediahub.R
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {

    companion object {
        var musicList: List<MusicAdapter.MediaMetadata>? = null
    }

    private lateinit var player: ExoPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var btnBack: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var songTitle: TextView
    private lateinit var tvTrackIndex: TextView

    private var currentPosition: Int = 0
    private var isUserTrackingSeekBar = false

    private val handler = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            if (::player.isInitialized && player.playbackState != Player.STATE_IDLE && !isUserTrackingSeekBar) {
                val current = player.currentPosition
                val duration = player.duration.coerceAtLeast(0L)

                if (duration > 0) {
                    val progressPercent = ((current.toFloat() / duration.toFloat()) * 1000).toInt()
                    seekBar.progress = progressPercent
                }

                startTime.text = formatTime(current)
                endTime.text = formatTime(duration)
            }
            handler.postDelayed(this, 500)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Find views
        btnBack = findViewById(R.id.btnBack)
        tvTrackIndex = findViewById(R.id.tvTrackIndex)
        songTitle = findViewById(R.id.song_title)
        playPauseButton = findViewById(R.id.playPauseButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        seekBar = findViewById(R.id.seekBar)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)

        seekBar.max = 1000

        // Enable marquee scrolling on title
        songTitle.isSelected = true

        btnBack.setOnClickListener {
            finish()
        }

        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build()

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    playPauseButton.setImageResource(R.drawable.baseline_pause_circle_24)
                } else {
                    playPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playNext()
                }
            }
        })

        // Setup playlist & initial track
        val playlist = musicList
        currentPosition = intent.getIntExtra("POSITION", 0)

        if (playlist.isNullOrEmpty()) {
            val fallbackUri = intent.getStringExtra("MUSIC_URI")
            val fallbackTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Title"
            if (fallbackUri != null) {
                musicList = listOf(MusicAdapter.MediaMetadata(fallbackTitle, fallbackUri, 0L))
                currentPosition = 0
            } else {
                finish()
                return
            }
        }

        playMusicAt(currentPosition)

        // Controls
        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }

        nextButton.setOnClickListener {
            playNext()
        }

        prevButton.setOnClickListener {
            playPrevious()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser && player.duration > 0) {
                    val targetMs = (progress.toLong() * player.duration) / 1000L
                    startTime.text = formatTime(targetMs)
                }
            }

            override fun onStartTrackingTouch(sb: SeekBar?) {
                isUserTrackingSeekBar = true
            }

            override fun onStopTrackingTouch(sb: SeekBar?) {
                if (player.duration > 0) {
                    val targetMs = (sb!!.progress.toLong() * player.duration) / 1000L
                    player.seekTo(targetMs)
                }
                isUserTrackingSeekBar = false
            }
        })

        handler.post(updateProgressRunnable)
    }

    private fun playMusicAt(index: Int) {
        val list = musicList ?: return
        if (list.isEmpty()) return

        currentPosition = index.coerceIn(0, list.size - 1)
        val song = list[currentPosition]

        songTitle.text = song.title
        tvTrackIndex.text = "${currentPosition + 1} / ${list.size}"

        val mediaItem = MediaItem.fromUri(song.data)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        seekBar.progress = 0
        startTime.text = "0:00"
    }

    private fun playNext() {
        val list = musicList ?: return
        if (list.isEmpty()) return

        val nextIndex = (currentPosition + 1) % list.size
        playMusicAt(nextIndex)
    }

    private fun playPrevious() {
        val list = musicList ?: return
        if (list.isEmpty()) return

        // If played more than 3 seconds, restart current track; otherwise go to previous
        if (player.currentPosition > 3000L) {
            player.seekTo(0)
        } else {
            val prevIndex = if (currentPosition > 0) currentPosition - 1 else list.size - 1
            playMusicAt(prevIndex)
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSec = TimeUnit.MILLISECONDS.toSeconds(ms.coerceAtLeast(0L))
        val minutes = totalSec / 60
        val seconds = totalSec % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateProgressRunnable)
        player.release()
        if (isFinishing) {
            musicList = null
        }
    }
}