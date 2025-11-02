package com.example.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.databinding.ActivityMainBinding
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
 import com.example.musicplayer.ui.MusicViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
