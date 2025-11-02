package com.example.musicplayer.repository

import android.content.Context
import com.example.musicplayer.data.AppDatabase
import com.example.musicplayer.data.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MusicRepository(private val context: Context, private val db: AppDatabase) {

    suspend fun downloadTrack(url: String, title: String, artist: String) {
        withContext(Dispatchers.IO) {
            val file = File(context.filesDir, "$title.mp3")
            URL(url).openStream().use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            val track = Track(title = title, artist = artist, filePath = file.absolutePath)
            db.trackDao().insertTrack(track)
        }
    }

    suspend fun getDownloadedTracks(): List<Track> {
        return db.trackDao().getAllTracks()
    }
}
