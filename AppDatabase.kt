package com.example.musicplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Track::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}
