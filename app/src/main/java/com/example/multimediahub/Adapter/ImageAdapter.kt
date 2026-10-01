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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.multimediahub.Activity.ImageViewerActivity
import com.example.multimediahub.R

sealed class ImageListItem {
    data class Header(val title: String) : ImageListItem()
    data class Image(val path: String, val index: Int) : ImageListItem()
}

class ImageAdapter(
    private val context: Context,
    private val items: List<ImageListItem>,
    private val rawImageList: List<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_IMAGE = 1
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHeader: TextView = itemView.findViewById(R.id.tvDateHeader)
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    fun isHeader(position: Int): Boolean {
        return position in items.indices && items[position] is ImageListItem.Header
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ImageListItem.Header -> VIEW_TYPE_HEADER
            is ImageListItem.Image -> VIEW_TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_date_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_image, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ImageListItem.Header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.tvHeader.text = item.title
            }
            is ImageListItem.Image -> {
                val imageHolder = holder as ImageViewHolder

                Glide.with(imageHolder.itemView.context)
                    .load(item.path)
                    .override(300, 300)
                    .centerCrop()
                    .thumbnail(0.15f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade(150))
                    .placeholder(R.drawable.baseline_image_24)
                    .error(R.drawable.baseline_image_not_supported_24)
                    .into(imageHolder.imageView)

                imageHolder.itemView.setOnClickListener {
                    ImageViewerActivity.imageUrlsList = rawImageList
                    val intent = Intent(context, ImageViewerActivity::class.java).apply {
                        putExtra("position", item.index)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
