package com.example.musicplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MusicReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "PLAY" -> MusicService.mediaPlayer?.start()
            "PAUSE" -> MusicService.mediaPlayer?.pause()
            "STOP" -> {
                MusicService.mediaPlayer?.stop()
                context.stopService(Intent(context, MusicService::class.java))
            }
        }
    }
}
