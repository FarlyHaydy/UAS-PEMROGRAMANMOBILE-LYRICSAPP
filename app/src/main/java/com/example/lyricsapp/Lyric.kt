package com.example.lyricsapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lyric(
    val id: String,
    val title: String,
    val image_url: String,
    val lyrics: String,
    val artist: String
) : Parcelable
