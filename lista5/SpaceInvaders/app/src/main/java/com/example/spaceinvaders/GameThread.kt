package com.example.spaceinvaders

import android.graphics.Canvas
import android.view.SurfaceHolder

class GameThread(private val surfaceHolder: SurfaceHolder, private val gameSurfaceView: GameSurfaceView) : Thread() {

    private var running: Boolean = false
    private val targetFPS = 60
    private val targetTime = (1000000000.0 / targetFPS).toLong() // time in nanoseconds

    fun setRunning(isRunning: Boolean) {
        this.running = isRunning
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var totalTime = 0L
        var frameCount = 0
        var timeCount = 0L // time since the last render update

        while (running) {
            startTime = System.nanoTime()

            gameSurfaceView.update() // update game physics as often as possible

            val currentTime = System.nanoTime()
            if (currentTime - timeCount > targetTime) {
                var canvas: Canvas? = null
                try {
                    canvas = surfaceHolder.lockCanvas()
                    synchronized(surfaceHolder) {
                    gameSurfaceView.draw(canvas) // draw the game at a fixed interval
                    timeCount = currentTime
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                }
            }

            // Control the frame rate
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime > 0) {
                try {
                    sleep(waitTime / 1000000, (waitTime % 1000000).toInt()) // convert nanoseconds to milliseconds and nanoseconds for sleep function
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            totalTime += System.nanoTime() - startTime
            frameCount++
            if (frameCount == targetFPS) {
                frameCount = 0
                totalTime = 0
            }
        }
    }
}