package com.example.mycalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EventsActivity : AppCompatActivity() {

    var day = 0
    var month = 0
    var year = 0
    var db: AppDatabase? = null

    val getContent = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult() ) { result ->
        if (result.resultCode == RESULT_OK) {
            // add event to database
            val title = result.data?.getStringExtra("title")
            val description = result.data?.getStringExtra("description")
            if (title != null && description != null) {
                val event = Event(title, description, day, month, year)
                db!!.eventDao().insertAll(event)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)


        day = intent.getIntExtra("day", 0)
        month = intent.getIntExtra("month", 0)
        year = intent.getIntExtra("year", 0)

        db = AppDatabase.getDatabase(this)

        val title = findViewById<TextView>(R.id.dateTextView)
        title.text = "Events for $day/${month + 1}/$year"


        db!!.eventDao().getAllByDate(day, month, year).observe(this) { events ->
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            val adapter = EventAdapter(events)
            recyclerView.adapter = adapter
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab_addEvent)
        fab.setOnClickListener {
            // start AddEventActivity with selected date
            val intent = Intent(this, AddEventActivity::class.java)
            getContent.launch(intent)
        }
    }
}