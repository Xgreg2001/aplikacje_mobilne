package com.example.zad2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val submitLocationButton = findViewById<Button>(R.id.submitLocationButton)
        val locationEditText = findViewById<EditText>(R.id.locationEditText)

        // request POST_NOTIFICATIONS permission
        requestPermissions(arrayOf("android.permission.POST_NOTIFICATIONS"), 0)

        submitLocationButton.setOnClickListener {
            val location = locationEditText.text.toString()
            Log.d(TAG, "setOnClickListener: $location ")

            // Zapisz lokalizację do SharedPreferences
            val sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("location", location)
            editor.apply()

            // Rozpocznij usługę
            val intent = Intent(this, WeatherService::class.java)
            startService(intent)
        }

        val stopServiceButton: Button = findViewById(R.id.stopServiceButton)
        stopServiceButton.setOnClickListener {
            val serviceIntent = Intent(this, WeatherService::class.java)
            stopService(serviceIntent)
        }
    }

}