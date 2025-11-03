package com.example.musicplayer

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var currentMusic: MusicModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentMusic = intent.getParcelableExtra("music")!!
        binding.musicTitle.text = currentMusic.title
        binding.musicArtist.text = currentMusic.artist

        mediaPlayer = MediaPlayer.create(this, currentMusic.fileRes)

        binding.playPauseBtn.setOnClickListener {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.pause()
                binding.playPauseBtn.text = "Play"
          val serviceIntent = Intent(this, MusicService::class.java)
serviceIntent.action = "PAUSE"
startService(serviceIntent)
            
            } else {
                mediaPlayer!!.start()
                binding.playPauseBtn.text = "Pause"
          val serviceIntent = Intent(this, MusicService::class.java)
serviceIntent.action = "PLAY"
startService(serviceIntent)
            
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}

private var rotateAnimator: ObjectAnimator? = null

private fun startAlbumRotation(view: View) {
    rotateAnimator?.cancel()
    rotateAnimator = ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f).apply {
        duration = 12000L // 12s per revolution
        interpolator = LinearInterpolator()
        repeatCount = ValueAnimator.INFINITE
        start()
    }
}

private fun pauseAlbumRotation() {
    rotateAnimator?.pause()
}

private fun resumeAlbumRotation() {
    rotateAnimator?.resume()
}

private fun stopAlbumRotation() {
    rotateAnimator?.cancel()
    rotateAnimator = null
}

private fun crossfade(from: View, to: View, durationMs: Long = 300L) {
    to.alpha = 0f
    to.visibility = View.VISIBLE
    to.animate().alpha(1f).setDuration(durationMs).start()
    from.animate().alpha(0f).setDuration(durationMs).withEndAction {
        from.visibility = View.GONE
        from.alpha = 1f // reset in case reused
    }.start()
}




private var seekAnimator: ValueAnimator? = null

private fun animateSeekTo(seekBar: SeekBar, from: Int, to: Int, duration: Long = 300L) {
    seekAnimator?.cancel()
    seekAnimator = ValueAnimator.ofInt(from, to).apply {
        this.duration = duration
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            seekBar.progress = it.animatedValue as Int
        }
        start()
    }
}


