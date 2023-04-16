package com.example.mycalendar

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {

    var db: AppDatabase? = null
    var calendarView: CalendarView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        calendarView = findViewById(R.id.calendarView)

        calendarView!!.setOnDateChangeListener { _, year, month, day ->
            // start EventsActivity with selected date

            val intent = Intent(this, EventsActivity::class.java)
            intent.putExtra("day", day)
            intent.putExtra("month", month)
            intent.putExtra("year", year)
            startActivity(intent)
        }

        db = AppDatabase.getDatabase(this)
    }
}
