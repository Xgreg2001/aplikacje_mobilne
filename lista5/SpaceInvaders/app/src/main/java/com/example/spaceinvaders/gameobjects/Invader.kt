package com.example.spaceinvaders.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Invader(private val x: Int, private val y: Int, private val screenHeight: Int) {

    var isDead: Boolean = false
    private val speed = 0.2 // Change to suit your needs
    private var lastUpdateTime = System.nanoTime()
    private val width = screenHeight / 25;
    private val height = screenHeight / 25;

    private val paint = Paint().apply {
        color = Color.GREEN
    }

    private val rect: Rect = Rect(x, y, x + width, y + height) // change dimensions as needed

    fun update(currentTime: Long) {
        if (isDead) {
            return
        }
        val deltaTime = (currentTime - lastUpdateTime) / 1000000.0 // convert nanoseconds to milliseconds
        val offset = (speed * deltaTime).toInt()
        rect.offset(0, offset)
        lastUpdateTime = currentTime
    }

    fun draw(canvas: Canvas) {
        if (isDead) {
            return
        }
        canvas.drawRect(rect, paint)
    }

    fun isOffScreen(): Boolean {
        if (isDead) {
            return false
        }
        return rect.top >= screenHeight
    }

    fun shouldRemove(): Boolean {
        return isDead;
    }

    // We'll need this to check for collisions with bullets
    fun getRect(): Rect {
        return rect
    }
}