package com.example.musicplayer.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import com.example.musicplayer.data.AppDatabase
import com.example.musicplayer.repository.MusicRepository
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "music_db"
    ).build()

    private val repo = MusicRepository(application, db)
    val tracks = MutableLiveData<List<com.example.musicplayer.data.Track>>()

    fun loadTracks() {
        viewModelScope.launch {
            tracks.postValue(repo.getDownloadedTracks())
        }
    }

    fun downloadTrack(url: String, title: String, artist: String) {
        viewModelScope.launch {
            repo.downloadTrack(url, title, artist)
            loadTracks()
        }
    }
}
