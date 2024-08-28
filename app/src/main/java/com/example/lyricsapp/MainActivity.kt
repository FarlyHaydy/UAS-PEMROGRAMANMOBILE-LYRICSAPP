package com.example.lyricsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.lyricsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: LyricsViewModel by viewModels()
    private lateinit var lyricsAdapter: LyricsAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate called")

        title = getString(R.string.title_main)

        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel.lyrics.observe(this, Observer { lyrics ->
            lyricsAdapter = LyricsAdapter(
                lyrics,
                { lyric ->
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("LYRIC", lyric)
                    startActivity(intent)
                },
                { lyric ->
                    viewModel.addFavoriteLyric(lyric)
                    Toast.makeText(this, getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Added to favorites: ${lyric.title}")
                },
                null, // Not passing onDeleteClick for MainActivity
                false // showDeleteButton is false for MainActivity
            )
            binding.recyclerView.adapter = lyricsAdapter
        })

        if (viewModel.lyrics.value == null) {
            fetchLyrics()
        }

        // Button to open FavoriteActivity
        binding.openFavoritesButton.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchLyrics() {
        Log.d("MainActivity", "fetchLyrics called")
        val apiService = ApiService.create()

        fetchLyricsFromApi(apiService, "coldplay", "yellow", "https://upload.wikimedia.org/wikipedia/id/9/9b/Yellow_cover_art.JPG", "Coldplay")
        fetchLyricsFromApi(apiService, "ed sheeran", "shape of you", "https://upload.wikimedia.org/wikipedia/id/b/b4/Shape_Of_You_%28Official_Single_Cover%29_by_Ed_Sheeran.png", "Ed Sheeran")
        fetchLyricsFromApi(apiService, "the weeknd", "blinding lights", "https://upload.wikimedia.org/wikipedia/en/e/e6/The_Weeknd_-_Blinding_Lights.png", "The Weeknd")
        fetchLyricsFromApi(apiService, "billie eilish", "bad guy", "https://upload.wikimedia.org/wikipedia/id/3/33/Billie_Eilish_-_Bad_Guy.png", "Billie Eilish")
        fetchLyricsFromApi(apiService, "bruno mars", "uptown funk", "https://upload.wikimedia.org/wikipedia/en/0/0e/MarkRonsonUptownFunk.png", "Bruno Mars")
        fetchLyricsFromApi(apiService, "taylor swift", "shake it off", "https://upload.wikimedia.org/wikipedia/id/c/c4/Taylor_Swift_-_Shake_It_Off.png", "Taylor Swift")
        fetchLyricsFromApi(apiService, "lady gaga", "shallow", "https://upload.wikimedia.org/wikipedia/ro/9/97/Lady_Gaga_%26_Bradley_Cooper_-_Shallow.png", "Lady Gaga")
        fetchLyricsFromApi(apiService, "adele", "hello", "https://upload.wikimedia.org/wikipedia/en/8/85/Adele_-_Hello_%28Official_Single_Cover%29.png", "Adele")
        fetchLyricsFromApi(apiService, "maroon 5", "sugar", "https://upload.wikimedia.org/wikipedia/id/d/d1/Maroon_5_Sugar_cover.png", "Maroon 5")
        fetchLyricsFromApi(apiService, "justin bieber", "peaches", "https://upload.wikimedia.org/wikipedia/en/0/0a/Justin_Bieber_-_Peaches.png", "Justin Bieber")
        fetchLyricsFromApi(apiService, "shawn mendes", "stitches", "https://upload.wikimedia.org/wikipedia/en/7/7a/Shawn_Mendes_-_Stitches.png", "Shawn Mendes")
        fetchLyricsFromApi(apiService, "billie eilish", "ocean eyes", "https://upload.wikimedia.org/wikipedia/id/a/a2/Ocean_Eyes_%28Official_Single_Cover%29_by_Billie_Eilish.png", "Billie Eilish")
        fetchLyricsFromApi(apiService, "sia", "chandelier", "https://upload.wikimedia.org/wikipedia/id/3/32/Sia_Chandelier.png", "Sia")
        fetchLyricsFromApi(apiService, "drake", "god's plan", "https://upload.wikimedia.org/wikipedia/id/9/90/Scorpion_by_Drake.jpg", "Drake")
        fetchLyricsFromApi(apiService, "bts", "dynamite", "https://upload.wikimedia.org/wikipedia/commons/thumb/9/97/Dynamite_BTS_%28musical_group%29.svg/330px-Dynamite_BTS_%28musical_group%29.svg.png", "BTS")
        fetchLyricsFromApi(apiService, "olivia rodrigo", "drivers license", "https://upload.wikimedia.org/wikipedia/id/thumb/b/b0/Olivia-Rodrigo-Drivers-License.jpg/1024px-Olivia-Rodrigo-Drivers-License.jpg", "Olivia Rodrigo")
        fetchLyricsFromApi(apiService, "post malone", "circles", "https://upload.wikimedia.org/wikipedia/en/a/a5/Post_Malone_-_Circles.png", "Post Malone")
        fetchLyricsFromApi(apiService, "dua lipa", "don't start now", "https://upload.wikimedia.org/wikipedia/en/2/2b/Dua_Lipa_-_Don%27t_Start_Now.png", "Dua Lipa")
        fetchLyricsFromApi(apiService, "ariana grande", "7 rings", "https://upload.wikimedia.org/wikipedia/id/b/b7/Ariana_Grande_-_7_rings.png", "Ariana Grande")
        fetchLyricsFromApi(apiService, "katy perry", "roar", "https://upload.wikimedia.org/wikipedia/id/4/41/Katy_Perry_-_Roar.png", "Katy Perry")
        fetchLyricsFromApi(apiService, "the beatles", "let it be", "https://upload.wikimedia.org/wikipedia/en/2/25/LetItBe.jpg", "The Beatles") // Additional song
    }

    private fun fetchLyricsFromApi(apiService: ApiService, artist: String, title: String, imageUrl: String, artistName: String) {
        apiService.searchLyrics(artist, title).enqueue(ApiCallback("MainActivity") { response ->
            response.body()?.let { lyricsResponse ->
                val lyric = Lyric(
                    id = title,
                    title = title,
                    image_url = imageUrl,
                    lyrics = lyricsResponse.lyrics,
                    artist = artistName
                )
                viewModel.setLyrics(viewModel.lyrics.value.orEmpty() + lyric)
            } ?: run {
                Log.e("MainActivity", "No lyrics found in response for $title by $artist")
            }
        })
    }
}
