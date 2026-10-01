package com.example.multimediahub.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val currentMatrix = Matrix()
    private val baseMatrix = Matrix()

    private val minScale = 1.0f
    private val maxScale = 5.0f
    private var currentScale = 1.0f

    private var viewWidth = 0
    private var viewHeight = 0

    private val scaleDetector: ScaleGestureDetector
    private val gestureDetector: GestureDetector

    private var isScaling = false
    private var isDragging = false
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var activePointerId = MotionEvent.INVALID_POINTER_ID

    private var zoomAnimator: ValueAnimator? = null

    var onSingleTapListener: (() -> Unit)? = null

    init {
        scaleType = ScaleType.MATRIX
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
        gestureDetector = GestureDetector(context, GestureListener())
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (viewWidth > 0 && viewHeight > 0) {
            fitToScreen()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        fitToScreen()
    }

    fun fitToScreen() {
        val d = drawable ?: return
        val dw = d.intrinsicWidth.toFloat()
        val dh = d.intrinsicHeight.toFloat()
        if (dw <= 0f || dh <= 0f || viewWidth <= 0 || viewHeight <= 0) return

        zoomAnimator?.cancel()

        val scaleX = viewWidth.toFloat() / dw
        val scaleY = viewHeight.toFloat() / dh
        val fitScale = minOf(scaleX, scaleY)

        val redundantXSpace = (viewWidth.toFloat() - dw * fitScale) / 2f
        val redundantYSpace = (viewHeight.toFloat() - dh * fitScale) / 2f

        baseMatrix.reset()
        baseMatrix.postScale(fitScale, fitScale)
        baseMatrix.postTranslate(redundantXSpace, redundantYSpace)

        currentMatrix.set(baseMatrix)
        currentScale = 1.0f

        imageMatrix = currentMatrix
        invalidate()
    }

    fun resetZoom(animate: Boolean = false) {
        zoomAnimator?.cancel()
        if (animate && currentScale > 1.01f) {
            animateMatrix(currentMatrix, baseMatrix, 1.0f)
        } else {
            currentMatrix.set(baseMatrix)
            currentScale = 1.0f
            imageMatrix = currentMatrix
            invalidate()
        }
    }

    private fun getImageBounds(): RectF? {
        val d = drawable ?: return null
        val dw = d.intrinsicWidth.toFloat()
        val dh = d.intrinsicHeight.toFloat()
        if (dw <= 0f || dh <= 0f) return null

        val rect = RectF(0f, 0f, dw, dh)
        currentMatrix.mapRect(rect)
        return rect
    }

    private fun fixTranslation(matrix: Matrix): Matrix {
        val d = drawable ?: return matrix
        val rect = RectF(0f, 0f, d.intrinsicWidth.toFloat(), d.intrinsicHeight.toFloat())
        matrix.mapRect(rect)

        var deltaX = 0f
        var deltaY = 0f

        if (rect.width() <= viewWidth) {
            deltaX = (viewWidth - rect.width()) / 2f - rect.left
        } else {
            if (rect.left > 0f) {
                deltaX = -rect.left
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right
            }
        }

        if (rect.height() <= viewHeight) {
            deltaY = (viewHeight - rect.height()) / 2f - rect.top
        } else {
            if (rect.top > 0f) {
                deltaY = -rect.top
            } else if (rect.bottom < viewHeight) {
                deltaY = viewHeight - rect.bottom
            }
        }

        if (deltaX != 0f || deltaY != 0f) {
            matrix.postTranslate(deltaX, deltaY)
        }
        return matrix
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        if (currentScale <= 1.01f) return false
        val bounds = getImageBounds() ?: return false
        if (bounds.width() <= viewWidth + 1f) return false

        return if (direction > 0) {
            // Dragging left (towards next page) -> can scroll if content remains on right
            bounds.right > viewWidth + 1f
        } else {
            // Dragging right (towards previous page) -> can scroll if content remains on left
            bounds.left < -1f
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                zoomAnimator?.cancel()
                activePointerId = event.getPointerId(0)
                lastTouchX = event.x
                lastTouchY = event.y
                isDragging = false

                if (currentScale > 1.01f) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                zoomAnimator?.cancel()
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = event.findPointerIndex(activePointerId)
                if (pointerIndex != -1 && !isScaling) {
                    val x = event.getX(pointerIndex)
                    val y = event.getY(pointerIndex)
                    val dx = x - lastTouchX
                    val dy = y - lastTouchY

                    if (currentScale > 1.01f) {
                        val bounds = getImageBounds()
                        if (bounds != null) {
                            val canScrollX = if (dx < 0) {
                                bounds.right > viewWidth + 1f
                            } else if (dx > 0) {
                                bounds.left < -1f
                            } else {
                                false
                            }

                            if (canScrollX || bounds.height() > viewHeight) {
                                parent?.requestDisallowInterceptTouchEvent(true)
                            } else {
                                parent?.requestDisallowInterceptTouchEvent(false)
                            }
                        }

                        currentMatrix.postTranslate(dx, dy)
                        fixTranslation(currentMatrix)
                        imageMatrix = currentMatrix
                        invalidate()
                        isDragging = true
                    }

                    lastTouchX = x
                    lastTouchY = y
                }
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = event.actionIndex
                val pointerId = event.getPointerId(pointerIndex)
                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    lastTouchX = event.getX(newPointerIndex)
                    lastTouchY = event.getY(newPointerIndex)
                    activePointerId = event.getPointerId(newPointerIndex)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                activePointerId = MotionEvent.INVALID_POINTER_ID
                isDragging = false
                isScaling = false
                parent?.requestDisallowInterceptTouchEvent(false)
            }
        }

        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            isScaling = true
            zoomAnimator?.cancel()
            parent?.requestDisallowInterceptTouchEvent(true)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            var factor = detector.scaleFactor
            val targetScale = currentScale * factor

            if (targetScale > maxScale) {
                factor = maxScale / currentScale
                currentScale = maxScale
            } else if (targetScale < minScale) {
                factor = minScale / currentScale
                currentScale = minScale
            } else {
                currentScale = targetScale
            }

            currentMatrix.postScale(factor, factor, detector.focusX, detector.focusY)
            fixTranslation(currentMatrix)
            imageMatrix = currentMatrix
            invalidate()
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            isScaling = false
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            zoomAnimator?.cancel()
            val startMatrix = Matrix(currentMatrix)
            val targetMatrix = Matrix()

            val targetScale: Float
            if (currentScale > 1.01f) {
                targetMatrix.set(baseMatrix)
                targetScale = 1.0f
            } else {
                targetMatrix.set(startMatrix)
                val factor = 3.0f / currentScale
                targetMatrix.postScale(factor, factor, e.x, e.y)
                fixTranslation(targetMatrix)
                targetScale = 3.0f
            }

            animateMatrix(startMatrix, targetMatrix, targetScale)
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            performClick()
            onSingleTapListener?.invoke()
            return true
        }
    }

    private fun animateMatrix(startMatrix: Matrix, targetMatrix: Matrix, targetScale: Float) {
        val startValues = FloatArray(9)
        val endValues = FloatArray(9)
        startMatrix.getValues(startValues)
        targetMatrix.getValues(endValues)

        val animatedValues = FloatArray(9)

        zoomAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 250
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animator ->
                val fraction = animator.animatedValue as Float
                for (i in 0 until 9) {
                    animatedValues[i] = startValues[i] + (endValues[i] - startValues[i]) * fraction
                }
                currentMatrix.setValues(animatedValues)
                imageMatrix = currentMatrix
                invalidate()
            }
        }
        currentScale = targetScale
        zoomAnimator?.start()
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
