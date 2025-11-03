package com.example.musicplayer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicplayer.service.MusicService

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val serviceIntent = Intent(context, MusicService::class.java).apply { this.action = action }
        context.startService(serviceIntent)
    }
}
