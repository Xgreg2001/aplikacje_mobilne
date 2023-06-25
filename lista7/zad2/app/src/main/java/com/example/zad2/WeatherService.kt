package com.example.zad2

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.*
import android.text.format.DateUtils
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class WeatherService : Service() {
    private val TAG = "WeatherService"
    private lateinit var sharedPreferences: SharedPreferences
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val notificationId = 1

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
        sharedPreferences = getSharedPreferences("WeatherApp", MODE_PRIVATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        fetchWeather()
        return START_STICKY
    }

    private fun fetchWeather() {
        coroutineScope.launch {
            while (true) {
                val location = sharedPreferences.getString("location", "")
                Log.d(TAG, "fetchWeather: $location")
                if (!location.isNullOrEmpty()) {
                    val weatherData = getWeatherData(location)
                    Log.d(TAG, "fetchWeather: $weatherData")
                    val notification =
                        weatherData?.let { createNotification(it) }
                    with(NotificationManagerCompat.from(this@WeatherService)) {
                            Log.d(TAG, "fetchWeather: $notification")
                        if (ActivityCompat.checkSelfPermission(
                                this@WeatherService,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Log.d(TAG, "fetchWeather: no permission")
                            return@launch
                        }
                        if (notification != null) {
                            Log.d(TAG, "fetchWeather: notify")
                            notify(notificationId, notification)
                        }
                    }
                }
                Log.d(TAG, "fetchWeather: sleep")
                // wait 1 minute for next fetch
                delay(60000)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(NotificationManagerCompat.from(this)) {
            cancel(notificationId)
        }
        coroutineScope.cancel()
    }

    private suspend fun getWeatherData(location: String): WeatherData? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.weatherapi.com/v1/current.json?key=59945dd839554c4bacc171050232506&q=$location")
            .build()

        Log.d(TAG, "getWeatherData: $request")

        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.string()?.let { body ->
                        Log.d(TAG, "getWeatherData: $body")
                        val jsonObject = JSONObject(body)
                        val currentObject = jsonObject.getJSONObject("current")

                        val temperature = currentObject.getDouble("temp_c")
                        val humidity = currentObject.getInt("humidity")
                        val conditionObject = currentObject.getJSONObject("condition")
                        val description = conditionObject.getString("text")

                        WeatherData(temperature, humidity, description, System.currentTimeMillis())
                    }
                } else {
                    Log.d(TAG, "getWeatherData: failed to fetch weather data")
                    Log.d(TAG, "getWeatherData: ${response.body?.string()}")
                    null
                }
            }
        }
    }

    private fun createNotification(weatherData: WeatherData): Notification {
        val channelId = "weather_notification_channel"

        val name = "Weather Notifications"
        val descriptionText = "Notifications about current weather"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // convert time to hours and minutes
        val humanReadableTime = DateUtils.formatDateTime(
            this,
            weatherData.time,
            DateUtils.FORMAT_SHOW_TIME
        )

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Replace with your own notification icon
            .setContentTitle("Weather in ${sharedPreferences.getString("location", "")} at $humanReadableTime")
            .setContentText("Temperature: ${weatherData.temperature}, Humidity: ${weatherData.humidity}, Conditions: ${weatherData.description}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .build()
    }

    data class WeatherData(val temperature: Double, val humidity: Int, val description: String, val time: Long)
}