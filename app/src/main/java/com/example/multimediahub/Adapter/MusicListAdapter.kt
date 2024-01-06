package com.example.multimediahub.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.AudioModal
import com.example.multimediahub.MusicPlayerActivity
import com.example.multimediahub.MyMediaPlayer
import com.example.multimediahub.R
import java.util.ArrayList

class MusicListAdapter(
    private val songsList: ArrayList<AudioModal>,
    private val context: Context
) : RecyclerView.Adapter<MusicListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.musicitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val songData = songsList[position]
        holder.titleTextView.text = songData.title

        if (MyMediaPlayer.currentIndex == position) {
            holder.titleTextView.setTextColor(Color.parseColor("#FF0000"))
        } else {
            holder.titleTextView.setTextColor(Color.parseColor("#000000"))
        }

        holder.itemView.setOnClickListener {
            //navigate to another acitivty

            MyMediaPlayer.getInstance().reset()
            MyMediaPlayer.currentIndex = position
            val intent = Intent(context, MusicPlayerActivity::class.java)
            intent.putExtra("LIST", songsList)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.music_title_text)
        val iconImageView: ImageView = itemView.findViewById(R.id.icon_view)
    }
}