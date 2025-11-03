package com.example.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.ItemMusicBinding
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

class MusicAdapter(
    private val musicList: ArrayList<MusicModel>,
    private val onClick: (MusicModel) -> Unit
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    inner class MusicViewHolder(val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        holder.binding.title.text = music.title
        holder.binding.artist.text = music.artist
        holder.itemView.setOnClickListener { onClick(music) }
    val albumArt = music.albumArtUri
    if (albumArt != null) {
        Glide.with(holder.itemView.context)
            .load(Uri.parse(albumArt))
            .placeholder(R.drawable.ic_music_note)
            .into(holder.imageAlbumArt)
    } else {
        holder.imageAlbumArt.setImageResource(R.drawable.ic_music_note)
    }
}
    
    
    
    
    }

    override fun getItemCount() = musicList.size
}







...

 
