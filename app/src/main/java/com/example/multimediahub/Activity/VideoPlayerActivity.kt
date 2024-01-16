package com.example.multimediahub.Activity

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.example.multimediahub.R
import java.util.concurrent.TimeUnit

class VideoPlayerActivity : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var videoImageView: ImageView
    private lateinit var videoContainer: FrameLayout
    private lateinit var playPauseButton: ImageView
    private lateinit var nextButton: ImageView
    private lateinit var prevButton: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var replay5SecButton: ImageView
    private lateinit var skip5SecButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        // Initialize the player
        player = ExoPlayer.Builder(this).build()

        // Find views by their ID
        playerView = findViewById(R.id.exoPlayerView)
        videoImageView = findViewById(R.id.videoImageView)
        videoContainer = findViewById(R.id.videoContainer)

//        val customControllerView = LayoutInflater.from(this).inflate(R.layout.layout_media_controls, null)
        playPauseButton = findViewById(R.id.videoPlayPauseButton)
        nextButton = findViewById(R.id.videoNextButton)
        prevButton = findViewById(R.id.videoPrevButton)
        seekBar = findViewById(R.id.videoSeekBar)
        startTime = findViewById(R.id.videoStartTime)
        endTime = findViewById(R.id.videoEndTime)
        replay5SecButton = findViewById(R.id.replay5Sec)
        skip5SecButton = findViewById(R.id.skip5Sec)

        val videoPath = intent.getStringExtra("VIDEO_PATH") ?: return
        val videoTitle = intent.getStringExtra("VIDEO_TITLE") ?: "Unknown Title"

        findViewById<TextView>(R.id.videoTitle).text = videoTitle

        val mediaItem = MediaItem.fromUri(videoPath)
        player.setMediaItem(mediaItem)
        player.prepare()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                // Handle playback state changes if needed
            }
        })

        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                playPauseButton.setImageResource(R.drawable.outline_play_circle_24)
            } else {
                player.play()
                playPauseButton.setImageResource(R.drawable.outline_pause_circle_outline_24)
            }
        }

        nextButton.setOnClickListener {
            // Handle skip to next video
        }

        prevButton.setOnClickListener {
            // Handle skip to previous video
        }

        replay5SecButton.setOnClickListener {
            seekToCurrentPosition(-5000) // Rewind 5 seconds (5000 milliseconds)
        }

        skip5SecButton.setOnClickListener {
            seekToCurrentPosition(5000) // Forward 5 seconds (5000 milliseconds)
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
        val observer = ExoPlayerLifecycleObserver(player)
        lifecycle.addObserver(observer)

        runOnUiThread(object : Runnable {
            override fun run() {
                if (::player.isInitialized) {
                    val currentPosition = player.currentPosition
                    val currentPercent =
                        (currentPosition.toFloat() / player.duration.toFloat()) * 100
                    seekBar.progress = currentPercent.toInt()

                    // Update start time text view
                    startTime.text = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(currentPosition),
                        TimeUnit.MILLISECONDS.toSeconds(currentPosition) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(currentPosition)
                                )
                    )

                    // Update end time text view
                    endTime.text = String.format(
                        "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(player.duration),
                        TimeUnit.MILLISECONDS.toSeconds(player.duration) -
                                TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(player.duration)
                                )
                    )
                }
                seekBar.postDelayed(this, 1000)
            }
        })

        // Load and set video thumbnail to ImageView
        Glide.with(this)
            .load(videoPath) // You can use the actual thumbnail path or generate one
            .into(videoImageView)

        // Attach the player to the PlayerView
        playerView.player = player
    }

    private class ExoPlayerLifecycleObserver(private val player: ExoPlayer) :
        LifecycleEventObserver {

        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            when (event) {
                Lifecycle.Event.ON_DESTROY -> player.release()
                else -> {
                    // Handle other lifecycle events if needed
                }
            }
        }
    }

    private fun seekToCurrentPosition(offsetMillis: Long) {
        val currentPosition = player.currentPosition
        val newPosition = currentPosition + offsetMillis
        player.seekTo(newPosition.coerceIn(0, player.duration))
    }
}
