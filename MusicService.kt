package com.example.musicplayer

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.musicplayer.MainActivity







class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private val CHANNEL_ID = "music_channel"

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.sample_music) // sample track
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            "PLAY" -> playMusic()
            "PAUSE" -> pauseMusic()
            "STOP" -> stopSelf()
        }
        return START_STICKY
    }

    private fun playMusic() {
        mediaPlayer.start()
        showNotification("Playing Music 🎶", true)
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            showNotification("Paused ⏸", false)
        }
    }

    private fun showNotification(status: String, isPlaying: Boolean) {
        val playIntent = Intent(this, MusicService::class.java).setAction("PLAY")
        val pauseIntent = Intent(this, MusicService::class.java).setAction("PAUSE")
        val stopIntent = Intent(this, MusicService::class.java).setAction("STOP")

        val pendingPlay = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
        val pendingPause = PendingIntent.getService(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
        val pendingStop = PendingIntent.getService(this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Music Player")
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_music_note)
            .addAction(R.drawable.ic_play, "Play", pendingPlay)
            .addAction(R.drawable.ic_pause, "Pause", pendingPause)
            .addAction(R.drawable.ic_stop, "Stop", pendingStop)
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Music Playback Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}


private fun createNotification(): Notification {
    val playIntent = Intent(this, MusicReceiver::class.java).setAction("PLAY")
    val pauseIntent = Intent(this, MusicReceiver::class.java).setAction("PAUSE")
    val stopIntent = Intent(this, MusicReceiver::class.java).setAction("STOP")

    val playPending = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE)
    val pausePending = PendingIntent.getBroadcast(this, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE)
    val stopPending = PendingIntent.getBroadcast(this, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE)

    val intent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    return NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("🎶 Music Player")
        .setContentText("Now Playing")
        .setSmallIcon(R.drawable.ic_music_note)
        .setContentIntent(pendingIntent)
        .addAction(R.drawable.ic_play, "Play", playPending)
        .addAction(R.drawable.ic_pause, "Pause", pausePending)
        .addAction(R.drawable.ic_stop, "Stop", stopPending)
        .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
        .setOngoing(true)
        .build()
}
