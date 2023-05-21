package com.example.spaceinvaders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spaceinvaders.gameobjects.Bullet
import com.example.spaceinvaders.gameobjects.Invader
import com.example.spaceinvaders.gameobjects.SpaceShip
import com.example.spaceinvaders.storage.GameData
import com.example.spaceinvaders.storage.GameDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    var thread: GameThread
    private lateinit var spaceShip: SpaceShip
    private val bullets = mutableListOf<Bullet>()
    private val bulletSpawnTime = 1_000000000L // seconds
    private var lastShootTime = System.nanoTime()
    private val invaders = mutableListOf<Invader>()
    private val invaderSpawnTime = 2_000000000L // seconds
    private var lastInvaderSpawnTime = System.nanoTime()

    var score = 0

    private var screenHeight = 0

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            spaceShip.update(currentX)
            handler.postDelayed(this, 1000 / 60) // run 60 times per second
        }
    }
    private var currentX = 0

    private val TAG = "GameSurfaceView"

    init {
        holder.addCallback(this)
        thread = GameThread(holder, this)
        spaceShip = SpaceShip(holder.surfaceFrame)

        // Set the OnTouchListener
        setOnTouchListener { v, event ->
            currentX = event.x.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Start the repeating action
                    handler.post(updateRunnable)
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Continue the repeating action with the new x-coordinate
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Stop the repeating action
                    handler.removeCallbacks(updateRunnable)
                    true
                }
                else -> false
            }
        }
    }

override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        thread.setRunning(true)
        thread.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenHeight = h
        spaceShip = SpaceShip(Rect(0, 0, w, h)) // Adjust as necessary
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {}

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun update() {
        // Update game state here (moving objects, collision detection, etc.)

        val now = System.nanoTime()
        if (now - lastShootTime >= bulletSpawnTime) {
            bullets.add(Bullet(spaceShip.x, spaceShip.y - spaceShip.shipRect.height()/2))
            lastShootTime = now
        }

        if (now - lastInvaderSpawnTime >= invaderSpawnTime) {
            val randomX = ((screenHeight / 25)..(width - (screenHeight / 25))).random()
            invaders.add(Invader(randomX, 0, screenHeight))
            lastInvaderSpawnTime = now
        }

        bullets.forEach { it.update(now) }
        invaders.forEach { it.update(now) }

        // Check for collisions
        bullets.forEach { bullet ->
            invaders.forEach { invader ->
                if (Rect.intersects(bullet.getRect(), invader.getRect())) {
                    Log.d(TAG, "Collision detected!")
                    // Remove the bullet and invader
                    bullet.isDead = true
                    invader.isDead = true
                    score++
                }
            }
        }

        // Remove dead bullets and invaders
        bullets.removeAll { it.shouldRemove() }
        invaders.removeAll { it.shouldRemove() }

        // Check for game over
        if (invaders.any { it.isOffScreen() }) {
            Log.d(TAG, "Game over!")
            thread.setRunning(false)
            (context as MainActivity).runOnUiThread {
                (context as MainActivity).showEndGameDialog(score)
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        // Draw game objects here
        spaceShip.draw(canvas)
        bullets.forEach { it.draw(canvas) }
        invaders.forEach { it.draw(canvas) }

        // Draw score
        canvas.drawText("Score: $score", 80f, 80f, Paint().apply { textSize = 80f; color = Color.WHITE })
    }

    fun reset() {
        score = 0
        spaceShip = SpaceShip(holder.surfaceFrame)
        bullets.clear()
        invaders.clear()
        lastShootTime = System.nanoTime()
        lastInvaderSpawnTime = System.nanoTime()
        thread = GameThread(holder, this)
        thread.setRunning(true)
        thread.start()
    }

}
