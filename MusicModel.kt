package com.example.musicplayer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicModel(
    val title: String,
    val artist: String,
    val fileRes: Int
) : Parcelable
