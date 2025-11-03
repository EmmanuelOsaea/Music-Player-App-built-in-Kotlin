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
 
        // shared element enter transition listener
        postponeEnterTransition()
        binding.ivAlbumArt.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        
        
        
        
        
        
        // Sample data
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
    }


// start rotation when started
        startAlbumRotation(binding.ivAlbumArt)

        // button micro animations
        binding.btnNext.setOnTouchListener(scaleTouchListener)
        binding.btnPrev.setOnTouchListener(scaleTouchListener)

        // seekbar periodic update with smooth animate
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
    }
}

binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MusicAdapter(musicList) { music ->
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("music", music)
            startActivity(intent)
        }
    }
}

// inside ViewHolder click
holder.itemView.setOnClickListener { view ->
    val intent = Intent(view.context, PlayerActivity::class.java)
    intent.putExtra("songIndex", position)

    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
        view.context as Activity,
        Pair.create(holder.binding.imageAlbumArt as View, "albumArtTransition")
    )
    view.context.startActivity(intent, options.toBundle())
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
    // Update RecyclerView adapter here
    adapter.updateSongs(songs)
}

fun updateSongs(newList: List<Song>) {
    songs.clear()
    songs.addAll(newList)
    notifyDataSetChanged()
}
