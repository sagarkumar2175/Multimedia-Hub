package com.example.multimediahub.Activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.multimediahub.R
import java.util.concurrent.TimeUnit

class MusicPlayerActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var seekBar: SeekBar
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        // Initialize the player
        player = ExoPlayer.Builder(this).build()

        // Find views by their ID
        playPauseButton = findViewById(R.id.playPauseButton)
        nextButton = findViewById(R.id.nextButton)
        prevButton = findViewById(R.id.prevButton)
        seekBar = findViewById(R.id.seekBar)
        startTime = findViewById(R.id.startTime)
        endTime = findViewById(R.id.endTime)


        val musicUri = intent.getStringExtra("MUSIC_URI") ?: return
        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Title"


        findViewById<TextView>(R.id.song_title).text = songTitle



        val mediaItem = MediaItem.fromUri(musicUri)
        player.setMediaItem(mediaItem)
        player.prepare()


        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)

            }
        })


        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                playPauseButton.setImageResource(R.drawable.baseline_play_circle_24)
            } else {
                player.play()
                playPauseButton.setImageResource(R.drawable.baseline_pause_circle_24)
            }
        }


        nextButton.setOnClickListener {
            // Handle skip to next track
        }

        prevButton.setOnClickListener {
            // Handle skip to previous track
        }

        // Setup SeekBar and other UI components
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo((progress * player.duration / 100).toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Update seekbar on timer thread
        runOnUiThread(object : Runnable {
            override fun run() {
                if (::player.isInitialized) {
                    val currentPosition = player.currentPosition
                    val currentPercent = (currentPosition.toFloat() / player.duration.toFloat()) * 100
                    seekBar.progress = currentPercent.toInt()

                    // Update start time text view
                    startTime.text = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentPosition))
                    )

                    // Update end time text view
                    endTime.text = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(player.duration),
                        TimeUnit.MILLISECONDS.toSeconds(player.duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(player.duration))
                    )
                }
                seekBar.postDelayed(this, 1000)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}