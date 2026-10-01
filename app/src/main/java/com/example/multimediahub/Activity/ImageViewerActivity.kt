package com.example.multimediahub.Activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.multimediahub.Adapter.ImageViewerAdapter
import com.example.multimediahub.R

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var topBar: View
    private lateinit var btnBack: ImageView
    private lateinit var tvImageCount: TextView

    private var currentPosition = 0
    private lateinit var imageUrls: List<String>
    private var isUiVisible = true

    companion object {
        private const val KEY_POSITION = "extra_current_position"
        var imageUrlsList: List<String> = emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        viewPager = findViewById(R.id.imageViewPager)
        topBar = findViewById(R.id.topBar)
        btnBack = findViewById(R.id.btnBack)
        tvImageCount = findViewById(R.id.tvImageCount)

        // Retrieve image URLs from the list
        imageUrls = imageUrlsList
        if (imageUrls.isEmpty()) {
            finish()
            return
        }

        // Restore position across rotation or from intent
        currentPosition = (savedInstanceState?.getInt(KEY_POSITION)
            ?: intent.getIntExtra("position", 0)).coerceIn(0, imageUrls.size - 1)

        btnBack.setOnClickListener {
            finish()
        }

        val adapter = ImageViewerAdapter(this, imageUrls) {
            toggleUiVisibility()
        }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
        viewPager.setCurrentItem(currentPosition, false)

        updateCounter(currentPosition)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position
                updateCounter(position)
            }
        })
    }

    private fun updateCounter(position: Int) {
        tvImageCount.text = "${position + 1} / ${imageUrls.size}"
    }

    private fun toggleUiVisibility() {
        isUiVisible = !isUiVisible
        if (isUiVisible) {
            topBar.animate()
                .alpha(1f)
                .setDuration(200)
                .withStartAction { topBar.visibility = View.VISIBLE }
                .start()
        } else {
            topBar.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { topBar.visibility = View.GONE }
                .start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_POSITION, viewPager.currentItem)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Only clear the list when finishing, so configuration changes (e.g. rotation) don't lose the list
        if (isFinishing) {
            imageUrlsList = emptyList()
        }
    }
}
