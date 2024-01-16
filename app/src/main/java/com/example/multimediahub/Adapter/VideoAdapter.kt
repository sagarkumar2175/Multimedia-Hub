package com.example.multimediahub.Adapter

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multimediahub.Activity.VideoPlayerActivity
import com.example.multimediahub.Fragment.VideoFragment
import com.example.multimediahub.R

class VideoAdapter(private val videoList: List<VideoFragment.VideoData>,val context: Context) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoImageView: ImageView = itemView.findViewById(R.id.videoImageView)
        val videoTextView: TextView = itemView.findViewById(R.id.videoTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val videoData = videoList[position]

        // Set video title to TextView
        holder.videoTextView.text = videoData.title

        // Load and set video thumbnail to ImageView
        holder.videoImageView.setImageBitmap(
            getThumbnail(
                holder.itemView.context,
                videoData.id
            )
        )
        // Open VideoPlayerActivity when a video item is clicked
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, VideoPlayerActivity::class.java)
            intent.putExtra("VIDEO_PATH", videoData.path)
            intent.putExtra("VIDEO_TITLE", videoData.title)
            holder.itemView.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return videoList.size
    }


    private fun getThumbnail(context: Context, videoId: Long): Bitmap? {
        val contentUri: Uri =
            ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId)
        val bitmap: Bitmap? = MediaStore.Video.Thumbnails.getThumbnail(
            context.contentResolver,
            videoId,

            MediaStore.Video.Thumbnails.MINI_KIND,
            null
        )
        return bitmap
    }
}
