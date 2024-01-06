package com.example.multimediahub.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.multimediahub.FullScreenActivity
import com.example.multimediahub.ImageModal
import com.example.multimediahub.R
import java.util.ArrayList

class ImageAdapter(private val list: ArrayList<ImageModal>, private val context: Context?) : RecyclerView.Adapter<ImageAdapter.ImageviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageviewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.imageitem, parent, false)

        return ImageviewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageviewHolder, position: Int) {
        Glide.with(context!!).load(list[position].getPath()).into(holder.imageView)

        holder.imageView.setOnClickListener {
            val parseData = list[position].getPath().toString()
            context.startActivity(Intent(context, FullScreenActivity::class.java).putExtra("parseData", parseData))
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ImageviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_item)
    }
}