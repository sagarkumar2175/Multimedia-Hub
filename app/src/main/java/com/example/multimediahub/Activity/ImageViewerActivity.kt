package com.example.multimediahub.Activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.multimediahub.R

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var fullImage: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        fullImage = findViewById(R.id.fullImage)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())

        val imageUrl = intent.getStringExtra("parseData")!!
        Glide.with(this).load(imageUrl).into(fullImage)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            fullImage.scaleX = scaleFactor
            fullImage.scaleY = scaleFactor

            return true
        }
    }
}
