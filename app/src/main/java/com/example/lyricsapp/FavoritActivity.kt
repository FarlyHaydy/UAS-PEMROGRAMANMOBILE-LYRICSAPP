package com.example.lyricsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lyricsapp.databinding.ActivityFavoriteBinding
import com.google.gson.Gson

class FavoriteActivity : AppCompatActivity() {

    private val viewModel: LyricsViewModel by viewModels()
    private lateinit var favoriteLyricsAdapter: LyricsAdapter
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.title_favorites)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel.favoriteLyrics.observe(this) { favoriteLyrics ->
            favoriteLyricsAdapter = LyricsAdapter(
                favoriteLyrics,
                { lyric ->
                    val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                    intent.putExtra("LYRIC", lyric)
                    startActivity(intent)
                },
                onDeleteClick = { lyric ->
                    viewModel.removeFavoriteLyric(lyric)
                    Toast.makeText(
                        this,
                        getString(R.string.removed_from_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                showDeleteButton = true
            )
            binding.recyclerView.adapter = favoriteLyricsAdapter
        }

        // Load favorite lyrics from SharedPreferences and set to ViewModel
        val sharedPreferences = getSharedPreferences("favorite_prefs", MODE_PRIVATE)
        val favoriteSet = sharedPreferences.getStringSet("favorites", emptySet())
        val favoriteList = favoriteSet?.map { json ->
            Gson().fromJson(json, Lyric::class.java)
        } ?: emptyList()
        viewModel.setFavoriteLyrics(favoriteList)
    }
}
