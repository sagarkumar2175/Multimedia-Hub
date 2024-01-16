package com.example.multimediahub.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multimediahub.Activity.ImageViewerActivity
import com.example.multimediahub.R

class ImageAdapter(private val context: Context, private val imageList: List<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Load image into ImageView using Glide
        Glide.with(holder.itemView.context)
            .load(imageList[position])
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.baseline_image_not_supported_24)
            .into(holder.imageView)

        // Handle item click to open the image viewer activity
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ImageViewerActivity::class.java)
            intent.putExtra("parseData", imageList[position])
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}

