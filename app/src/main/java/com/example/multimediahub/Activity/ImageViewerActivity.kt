package com.example.multimediahub.Activity

import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.multimediahub.R

class ImageViewerActivity : AppCompatActivity() {

    private lateinit var fullImage: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var gestureDetector: GestureDetector
    private var scaleFactor = 1.0f
    private var currentPosition = 0
    private lateinit var imageUrls: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        fullImage = findViewById(R.id.fullImage)
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        gestureDetector = GestureDetector(this, GestureListener())

        // Retrieve the image URLs from the intent
        imageUrls = intent.getStringArrayListExtra("imageUrls") ?: emptyList()

        // Get the current position from the intent
        currentPosition = intent.getIntExtra("position", 0)

        // Load the initial image
        loadImage(imageUrls[currentPosition])
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(fullImage)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.1f, 10.0f)

            fullImage.scaleX = scaleFactor
            fullImage.scaleY = scaleFactor

            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val animationDuration = 300L
        private val flingThreshold = 2500 // Adjust sensitivity as needed

        override fun onDoubleTap(e: MotionEvent): Boolean {
            // Double-tap gesture, reset the image size to normal
            scaleFactor = 1.0f
            fullImage.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(animationDuration).start()
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            // Fling gesture, handle left and right swipes to change images
            if (e1 != null && e2 != null) {
                val deltaX = e2.x - e1.x
                val deltaY = e2.y - e1.y
                val velocity = Math.sqrt((velocityX * velocityX + velocityY * velocityY).toDouble())

                if (velocity > flingThreshold) {
                    // Adjust sensitivity by increasing the flingThreshold value
                    if (deltaX > 0 && currentPosition > 0) {
                        // Swipe to the right
                        animateImageChange(imageUrls[currentPosition - 1], false)
                    } else if (deltaX < 0 && currentPosition < imageUrls.size - 1) {
                        // Swipe to the left
                        animateImageChange(imageUrls[currentPosition + 1], true)
                    }
                }
            }
            return true
        }

        private fun animateImageChange(imageUrl: String, toLeft: Boolean) {
            fullImage.animate().alpha(0f).setDuration(animationDuration / 2).withEndAction {
                loadImage(imageUrl)
                fullImage.alpha = 0f
                fullImage.animate().alpha(1f).setDuration(animationDuration / 2).start()
            }.start()

            if (toLeft) {
                currentPosition++
            } else {
                currentPosition--
            }

            // Reset the scale to original size for the new image
            scaleFactor = 1.0f
            fullImage.animate().scaleX(scaleFactor).scaleY(scaleFactor).setDuration(animationDuration).start()
        }
    }

}
