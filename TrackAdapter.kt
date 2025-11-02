package com.example.musicplayer.ui

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.data.Track
import com.example.musicplayer.databinding.ItemTrackBinding

class TrackAdapter : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    private var tracks: List<Track> = emptyList()
    private var mediaPlayer: MediaPlayer? = null

    fun submitList(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    inner class TrackViewHolder(private val binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.txtTitle.text = track.title
            binding.txtArtist.text = track.artist

            binding.btnPlay.setOnClickListener {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(track.filePath)
                    prepare()
                    start()
                }
            }

            binding.btnStop.setOnClickListener {
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = null
            }
        }
    }
}
