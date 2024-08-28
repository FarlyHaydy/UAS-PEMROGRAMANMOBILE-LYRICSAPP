package com.example.lyricsapp

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson

class LyricsViewModel(application: Application) : AndroidViewModel(application) {

    private val _lyrics = MutableLiveData<List<Lyric>>()
    val lyrics: LiveData<List<Lyric>> get() = _lyrics

    private val _favoriteLyrics = MutableLiveData<List<Lyric>>()
    val favoriteLyrics: LiveData<List<Lyric>> get() = _favoriteLyrics

    fun setLyrics(lyrics: List<Lyric>) {
        _lyrics.value = lyrics
    }

    fun addFavoriteLyric(lyric: Lyric) {
        val currentFavorites = _favoriteLyrics.value.orEmpty().toMutableList()
        if (!currentFavorites.contains(lyric)) {
            currentFavorites.add(lyric)
            _favoriteLyrics.value = currentFavorites
            saveFavoritesToSharedPreferences(currentFavorites)
        }
    }

    fun removeFavoriteLyric(lyric: Lyric) {
        val currentFavorites = _favoriteLyrics.value.orEmpty().toMutableList()
        if (currentFavorites.contains(lyric)) {
            currentFavorites.remove(lyric)
            _favoriteLyrics.value = currentFavorites
            saveFavoritesToSharedPreferences(currentFavorites)
        }
    }

    fun setFavoriteLyrics(favoriteLyrics: List<Lyric>) {
        _favoriteLyrics.value = favoriteLyrics
    }

    private fun saveFavoritesToSharedPreferences(favorites: List<Lyric>) {
        val sharedPreferences = getApplication<Application>().getSharedPreferences("favorite_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val favoriteSet = favorites.map { Gson().toJson(it) }.toSet()
        editor.putStringSet("favorites", favoriteSet)
        editor.apply()
    }
}
