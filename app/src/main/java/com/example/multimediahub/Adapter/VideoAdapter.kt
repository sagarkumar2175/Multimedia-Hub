package com.example.multimediahub.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.multimediahub.Activity.VideoPlayerActivity
import com.example.multimediahub.Fragment.VideoFragment
import com.example.multimediahub.R

sealed class VideoListItem {
    data class Header(val title: String) : VideoListItem()
    data class Video(val data: VideoFragment.VideoData, val rawIndex: Int) : VideoListItem()
}

class VideoAdapter(
    private val items: List<VideoListItem>,
    private val rawVideoList: List<VideoFragment.VideoData>,
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_VIDEO = 1
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvDateHeader)
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoImageView: ImageView = itemView.findViewById(R.id.videoImageView)
        val videoTextView: TextView = itemView.findViewById(R.id.videoTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is VideoListItem.Header -> VIEW_TYPE_HEADER
            is VideoListItem.Video -> VIEW_TYPE_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_date_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_video, parent, false)
            VideoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is VideoListItem.Header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.tvHeader.text = item.title
            }
            is VideoListItem.Video -> {
                val videoHolder = holder as VideoViewHolder
                val videoData = item.data

                videoHolder.videoTextView.text = videoData.title

                // Asynchronously decode and cache video thumbnail off the UI main thread
                Glide.with(videoHolder.itemView.context)
                    .asBitmap()
                    .load(videoData.path)
                    .override(180, 180)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(8)))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.drawable.baseline_video_file_24)
                    .error(R.drawable.baseline_video_file_24)
                    .into(videoHolder.videoImageView)

                videoHolder.itemView.setOnClickListener {
                    VideoPlayerActivity.videoList = rawVideoList
                    val intent = Intent(holder.itemView.context, VideoPlayerActivity::class.java).apply {
                        putExtra("position", item.rawIndex)
                    }
                    holder.itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
