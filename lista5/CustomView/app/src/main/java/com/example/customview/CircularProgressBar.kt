package com.example.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class CircularProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var progress: Float = 0f

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    private val rect = RectF()

    init {
        context.withStyledAttributes(attrs, R.styleable.CircularProgressBar) {
            progress = getFloat(R.styleable.CircularProgressBar_progress, 0f)
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        rect.set(0 + paint.strokeWidth / 2, 0 + paint.strokeWidth / 2, width - paint.strokeWidth / 2, height - paint.strokeWidth / 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.color = 0xFF888888.toInt() // Background color.
        canvas.drawOval(rect, paint)

        paint.color = 0xFF0000FF.toInt() // Progress color.
        canvas.drawArc(rect, -90f, progress * 360f / 100, false, paint)
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate() // Force the view to redraw.
    }
}
