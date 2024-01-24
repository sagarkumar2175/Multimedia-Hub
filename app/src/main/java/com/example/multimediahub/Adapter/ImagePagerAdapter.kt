package com.example.multimediahub.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.multimediahub.R

class ImagePagerAdapter(private val imageUrls: List<String>) : PagerAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_image_pager, container, false)
        val imageView = view.findViewById<ImageView>(R.id.fullImage)

        Glide.with(container.context)
            .load(imageUrls[position])
            .into(imageView)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
