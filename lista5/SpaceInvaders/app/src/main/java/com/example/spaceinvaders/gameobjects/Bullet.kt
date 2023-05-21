package com.example.spaceinvaders.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect

class Bullet(private val x: Int, private val y: Int) {

    var isDead: Boolean = false
    private val width = 10
    private val height = 20
    private var lastUpdateTime = System.nanoTime()
    val speed = 1 // Change to suit your needs

    private val paint = Paint().apply {
        color = Color.WHITE
    }

    private val rect: Rect = Rect(x, y, x + width, y + height) // change dimensions as needed

    fun update(currentTime: Long) {
        if (isDead) {
            return
        }
        val deltaTime = (currentTime - lastUpdateTime) / 1000000.0 // convert nanoseconds to milliseconds
        val offset = (speed * deltaTime).toInt()
        rect.offset(0, -offset)
        lastUpdateTime = currentTime
    }

    fun draw(canvas: Canvas) {
        if (isDead) {
            return
        }
        canvas.drawRect(rect, paint)
    }

    fun shouldRemove(): Boolean {
        if (isDead) {
            return true
        }
        return rect.bottom <= 0
    }

    fun getRect(): Rect {
        return rect
    }
}