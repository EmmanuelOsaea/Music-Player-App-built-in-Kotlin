com.example.smartmusicplayer.data

package com.example.smartmusicplayer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val artist: String,
    val album: String? = null,
    val duration: Long = 0,
    val filePath: String,   // Local storage or URI path
    val isFavorite: Boolean = false
)
