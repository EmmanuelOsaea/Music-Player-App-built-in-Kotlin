package com.example.musicplayer.ui

import android.media.MediaPlayer
import android.os.*
import android.view.*
import android.widget.*
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AppCompatDelegate
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var musicList: ArrayList<MusicModel>
    private val viewModel: MusicViewModel by viewModels()
    private lateinit var adapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1️⃣ Transition setup
        postponeEnterTransition()
        binding.ivAlbumArt.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        // 2️⃣ RecyclerView + LiveData
        adapter = SongAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allSongs.observe(this) { songs ->
            adapter.submitList(songs)
            startPostponedEnterTransition()
        }

        // 3️⃣ Media player setup
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

        // 4️⃣ UI extras
        startAlbumRotation(binding.ivAlbumArt)

        binding.btnNext.setOnTouchListener(scaleTouchListener)
        binding.btnPrev.setOnTouchListener(scaleTouchListener)

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

        // 5️⃣ Optional features
        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).setAction("PLAY")
            startService(intent)
        }

        binding.btnPause.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).setAction("PAUSE")
            startService(intent)
        }

        binding.btnStop.setOnClickListener {
            val intent = Intent(this, MusicService::class.java).setAction("STOP")
            startService(intent)
        }

        val equalizerBtn = findViewById<Button>(R.id.btnEqualizer)
        equalizerBtn.setOnClickListener {
            startActivity(Intent(this, EqualizerActivity::class.java))
        }

        val switchTheme = findViewById<Switch>(R.id.switchTheme)
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private val scaleTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { v.scaleX = 0.96f; v.scaleY = 0.96f }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> { v.scaleX = 1f; v.scaleY = 1f }
        }
        false
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        stopAlbumRotation()
    }

    // 🔒 Permissions and helpers stay inside class
    private val PERMISSION_REQUEST_CODE = 100

    private fun checkPermission() {
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadSongs()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadSongs()
        }
    }

    private fun loadSongs() {
        val songs = MusicScanner.getAllAudioFiles(this)
        adapter.updateSongs(songs)
    }
}


