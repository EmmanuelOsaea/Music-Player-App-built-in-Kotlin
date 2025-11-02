package com.example.musicplayer.data

import androidx.room.*

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<Track>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    @Delete
    suspend fun deleteTrack(track: Track)
}
