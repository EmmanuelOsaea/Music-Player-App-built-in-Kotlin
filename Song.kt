package com.example.musicplayer.model
package com.example.musicplayer.model

data class Song(
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long
)

data class Song(
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long,
    val albumArtUri: String? = null
)
