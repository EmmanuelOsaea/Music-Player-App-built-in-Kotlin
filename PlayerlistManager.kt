package com.example.musicplayer

import android.content.Context
import android.content.SharedPreferences

object PlaylistManager {
    private const val PREF_NAME = "playlist_prefs"
    private const val KEY_FAVORITES = "favorites"

    fun addToFavorites(context: Context, title: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val set = prefs.getStringSet(KEY_FAVORITES, mutableSetOf())!!.toMutableSet()
        set.add(title)
        prefs.edit().putStringSet(KEY_FAVORITES, set).apply()
    }

    fun getFavorites(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
    }
}
