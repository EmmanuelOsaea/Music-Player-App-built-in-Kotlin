package com.example.musicplayer

import android.media.MediaPlayer
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.ui.MusicViewModel
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
     private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private lateinit var musicList: ArrayList<MusicModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
    }
      
        
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
    }
}
private val viewModel: MusicViewModel by viewModels()

      val adapter = TrackAdapter()
        binding.recyclerTracks.layoutManager = LinearLayoutManager(this)
        binding.recyclerTracks.adapter = adapter

        viewModel.tracks.observe(this) {
            adapter.submitList(it)
        }

        binding.btnDownload.setOnClickListener {
            val url = binding.inputUrl.text.toString()
            val title = binding.inputTitle.text.toString()
            val artist = binding.inputArtist.text.toString()
            viewModel.downloadTrack(url, title, artist)
        }

        viewModel.loadTracks()
    }
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

val intent = Intent(this, MusicService::class.java)
intent.putExtra("song_res", R.raw.sample_music) // replace with actual song
startForegroundService(intent)

