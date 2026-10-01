package com.example.multimediahub.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multimediahub.R
import com.example.multimediahub.widget.ZoomableImageView

class ImageViewerAdapter(
    private val context: Context,
    private val imageUrls: List<String>,
    private val onImageSingleTap: (() -> Unit)? = null
) : RecyclerView.Adapter<ImageViewerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val zoomableImageView: ZoomableImageView = itemView.findViewById(R.id.zoomableImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fullscreen_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.zoomableImageView.resetZoom()
        holder.zoomableImageView.onSingleTapListener = onImageSingleTap

        Glide.with(context)
            .load(imageUrls[position])
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.zoomableImageView)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.zoomableImageView.resetZoom()
    }

    override fun getItemCount(): Int = imageUrls.size
}
