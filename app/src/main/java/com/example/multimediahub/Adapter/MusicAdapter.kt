package com.example.multimediahub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.R

class MusicAdapter(
    private val musicList: List<MediaMetadata>,
    private val itemClickListener: (MediaMetadata) -> Unit
) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicTextView: TextView = itemView.findViewById(R.id.musicTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val metadata = musicList[position]
        holder.musicTextView.text = metadata.title
        holder.itemView.setOnClickListener { itemClickListener(metadata) }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
    data class MediaMetadata(
        val title: String,
        val data: String,
        val duration: Long
    )

}

