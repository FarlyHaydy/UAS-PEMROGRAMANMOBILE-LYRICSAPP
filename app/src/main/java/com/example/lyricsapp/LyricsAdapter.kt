package com.example.lyricsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lyricsapp.databinding.ItemLyricBinding
import com.squareup.picasso.Picasso

class LyricsAdapter(
    private val lyrics: List<Lyric>,
    private val onClick: (Lyric) -> Unit,
    private val onLoveClick: ((Lyric) -> Unit)? = null,
    private val onDeleteClick: ((Lyric) -> Unit)? = null,
    private val showDeleteButton: Boolean = false
) : RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsViewHolder {
        val binding = ItemLyricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LyricsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricsViewHolder, position: Int) {
        holder.bind(lyrics[position])
    }

    override fun getItemCount() = lyrics.size

    inner class LyricsViewHolder(private val binding: ItemLyricBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) {
            binding.titleTextView.text = lyric.title
            binding.subtitleTextView.text = "Artist: ${lyric.artist}"
            Picasso.get().load(lyric.image_url).into(binding.imageView)
            itemView.setOnClickListener { onClick(lyric) }

            if (showDeleteButton) {
                binding.deleteButton.visibility = View.VISIBLE
                binding.loveButton.visibility = View.GONE
                binding.deleteButton.setOnClickListener { onDeleteClick?.invoke(lyric) }
            } else {
                binding.deleteButton.visibility = View.GONE
                binding.loveButton.visibility = View.VISIBLE
                binding.loveButton.setOnClickListener { onLoveClick?.invoke(lyric) }
            }
        }
    }
}