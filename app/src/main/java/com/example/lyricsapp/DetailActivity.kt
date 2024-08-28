package com.example.lyricsapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        Log.d("DetailActivity", "onCreate called")

        title = getString(R.string.title_detail)

        val textView = findViewById<TextView>(R.id.textView)
        val imageView = findViewById<ImageView>(R.id.imageView)

        val lyric = intent.getParcelableExtra<Lyric>("LYRIC")
        Log.d("DetailActivity", "Received lyric: $lyric")
        lyric?.let {
            textView.text = it.lyrics
            Picasso.get().load(it.image_url).into(imageView)
        }
    }
}
