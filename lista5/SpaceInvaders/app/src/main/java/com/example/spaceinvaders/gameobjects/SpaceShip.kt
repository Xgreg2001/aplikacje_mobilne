package com.example.spaceinvaders.gameobjects

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log

class SpaceShip(private val screenRect: Rect) {
    private val paint = Paint()
    val shipRect = Rect()
    private val speed = 20 // Change to suit your needs
    var x = shipRect.centerX()
    var y = shipRect.centerY()
    val bottomOffSet = 200

    private val TAG = "SpaceShip"

    init {
        // Assuming the spaceship is 1/10th the width of the screen and the height is half the width
        val shipWidth = screenRect.width() / 10
        val shipHeight = shipWidth

        Log.d("INIT", "init: screen width: ${screenRect.width()}, screen height: ${screenRect.height()}")

        // Place the spaceship in the middle and at the bottom of the screen
        val left = screenRect.width() / 2 - shipWidth / 2
        val top = screenRect.height() -  shipHeight - bottomOffSet
        val right = screenRect.width() / 2 + shipWidth / 2
        val bottom = screenRect.height() - bottomOffSet
        shipRect.set(left, top, right, bottom)


        paint.color = Color.RED // Change to suit your needs
    }

    fun draw(canvas: Canvas) {
        Log.d(TAG, "draw: rect width: ${shipRect.width()}, rect height: ${shipRect.height()}")
        canvas.drawRect(shipRect, paint)
    }

    fun update(newX: Int) {
        // Calculate the center of the spaceship
        val shipCenterX = shipRect.left + shipRect.width() / 2

        // Calculate the new left and right of the spaceship
        var left = newX - shipRect.width() / 2
        var right = newX + shipRect.width() / 2

        val newShipCenterX = left + shipRect.width() / 2

        // ship should't move further than speed

        if (kotlin.math.abs(newShipCenterX - shipCenterX) > speed) {
            if (newShipCenterX < shipCenterX){
                left = shipCenterX - speed - shipRect.width() / 2
                right = shipCenterX - speed + shipRect.width() / 2
            } else {
                left = shipCenterX + speed - shipRect.width() / 2
                right = shipCenterX + speed + shipRect.width() / 2
            }

        }

        // Don't move beyond the screen
        if (left < 0) {
            left = 0
            right = shipRect.width()
        }
        if (right > screenRect.width()) {
            left = screenRect.width() - shipRect.width()
            right = screenRect.width()
        }

        // Update the position of the spaceship
        shipRect.set(left, shipRect.top, right, shipRect.bottom)

        x = shipRect.centerX()
        y = shipRect.centerY()
    }
}