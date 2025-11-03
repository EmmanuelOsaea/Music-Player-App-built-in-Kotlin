package com.example.musicplayer.utils

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import com.example.musicplayer.model.Song

object MusicScanner {

    fun getAllAudioFiles(context: Context): List<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor: Cursor? = context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            "${MediaStore.Audio.Media.TITLE} ASC"
        )

        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val title = it.getString(titleIndex)
                val artist = it.getString(artistIndex)
                val path = it.getString(dataIndex)
                val duration = it.getLong(durationIndex)

                songs.add(Song(title, artist, path, duration))
            }
        }

        return songs
    }
}
