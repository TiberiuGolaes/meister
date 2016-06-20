package com.nalatarate.meister.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

/**
 * Created by Tiberiu on 6/19/2016.
 */
abstract class MeasuredImageView : ImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    protected var canvasSize: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    protected open fun measureWidth(measureSpec: Int) = measure(measureSpec)
    protected open fun measureHeight(measureSpec: Int) = measure(measureSpec)

    private fun measure(measureSpec: Int): Int {
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        when (specMode) {
            View.MeasureSpec.EXACTLY, View.MeasureSpec.AT_MOST -> return specSize
            else -> return canvasSize
        }
    }

    protected fun drawableToBitmap(drawable: Drawable?): Bitmap? {
        if (drawable == null)
        // Don't do anything without a proper drawable
            return null
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val intrinsicWidth = drawable.intrinsicWidth
        val intrinsicHeight = drawable.intrinsicHeight

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
            return null

        try {
            // Create Bitmap object out of the drawable
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)//Bitmap.Config.RGB_565);
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: OutOfMemoryError) {
            return null
        }

    }

}