package com.example.musicplayer.ui

import android.media.MediaPlayer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.databinding.ActivityMainBinding
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var musicList: ArrayList<MusicModel>
    private val viewModel: MusicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1️⃣ Shared element enter transition
        postponeEnterTransition()
        binding.ivAlbumArt.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        // 2️⃣ RecyclerView + ViewModel setup
        val adapter = SongAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.allSongs.observe(this) { songs ->
            adapter.submitList(songs)
            startPostponedEnterTransition()
        }

        // 3️⃣ MediaPlayer setup
        musicList = arrayListOf(
            MusicModel("Ocean Waves", "Meditation", R.raw.sample_song),
            MusicModel("Calm Mind", "Relax", R.raw.sample_song),
            MusicModel("Focus Flow", "Study", R.raw.sample_song)
        )

        mediaPlayer = MediaPlayer.create(this, R.raw.sample_song)

        binding.playButton.setOnClickListener {
            if (isPlaying) {
                mediaPlayer?.pause()
                binding.playButton.text = "Play"
            } else {
                mediaPlayer?.start()
                binding.playButton.text = "Pause"
            }
            isPlaying = !isPlaying
        }

        // 4️⃣ Start album rotation
        startAlbumRotation(binding.ivAlbumArt)

        // 5️⃣ Button touch animations
        binding.btnNext.setOnTouchListener(scaleTouchListener)
        binding.btnPrev.setOnTouchListener(scaleTouchListener)

        // 6️⃣ SeekBar periodic update
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    val pos = it.currentPosition
                    val max = it.duration
                    binding.seekBar.max = max
                    animateSeekTo(binding.seekBar, binding.seekBar.progress, pos)
                }
                handler.postDelayed(this, 500)
            }
        })

        // 7️⃣ Equalizer button
        binding.btnEqualizer.setOnClickListener {
            startActivity(Intent(this, EqualizerActivity::class.java))
        }

        // 8️⃣ Theme switch
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private val scaleTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> v.scaleX = 0.96f; v.scaleY = 0.96f
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.scaleX = 1f; v.scaleY = 1f
        }
        false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        stopAlbumRotation()
    }
}
