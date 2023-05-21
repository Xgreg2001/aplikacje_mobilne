package com.example.customview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: CircularProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.circularProgressBar)

        // Animate progress bar
        Handler(Looper.getMainLooper()).postDelayed({
            for (i in 0..100) {
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.setProgress(i.toFloat())
                }, (i * 100).toLong())
            }
        }, 1000)
    }
}
