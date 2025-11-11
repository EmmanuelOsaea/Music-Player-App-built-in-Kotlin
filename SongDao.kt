com.example.smartmusicplayer.data

package com.example.smartmusicplayer.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SongDao {

    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): LiveData<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: SongEntity)

    @Update
    suspend fun update(song: SongEntity)

    @Delete
    suspend fun delete(song: SongEntity)

    @Query("UPDATE songs SET isFavorite = :favorite WHERE id = :songId")
    suspend fun updateFavoriteStatus(songId: Int, favorite: Boolean)
}
