package com.example.musicplayer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.chibde.visualizer.BarVisualizer
import com.example.musicplayer.R

class NowPlayingActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var progressBar: ProgressBar
    private lateinit var visualizer: BarVisualizer
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_playing)

        val songRes = R.raw.sample_music
        val songTitle = "Sample Track"

        val tvSongTitle = findViewById<TextView>(R.id.tvSongTitle)
        val albumArt = findViewById<ImageView>(R.id.ivAlbumArt)
        val playPause = findViewById<ImageButton>(R.id.btnPlayPause)
        val next = findViewById<ImageButton>(R.id.btnNext)
        val prev = findViewById<ImageButton>(R.id.btnPrev)
        progressBar = findViewById(R.id.progressBar)
        visualizer = findViewById(R.id.barVisualizer)

        tvSongTitle.text = songTitle

        mediaPlayer = MediaPlayer.create(this, songRes)
        visualizer.setPlayer(mediaPlayer.audioSessionId)

        playPause.setOnClickListener {
            if (isPlaying) {
                mediaPlayer.pause()
                playPause.setImageResource(R.drawable.ic_play)
            } else {
                mediaPlayer.start()
                playPause.setImageResource(R.drawable.ic_pause)
            }
            isPlaying = !isPlaying
        }

        next.setOnClickListener {
            // skip to next song logic
        }

        prev.setOnClickListener {
            // skip to previous song logic
        }

        Thread {
            while (mediaPlayer.isPlaying) {
                runOnUiThread {
                    progressBar.progress =
                        ((mediaPlayer.currentPosition.toFloat() / mediaPlayer.duration) * 100).toInt()
                }
                Thread.sleep(500)
            }
        }.start()
    }

    override fun onDestroy() {
        visualizer.release()
        mediaPlayer.release()
        super.onDestroy()
    }
}
