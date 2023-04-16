package com.example.mycalendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class AddEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        val submit_button = findViewById<android.widget.Button>(R.id.submit_button)

        submit_button.setOnClickListener {
            val title = findViewById<android.widget.EditText>(R.id.textInputTitle).text.toString()
            val description = findViewById<android.widget.EditText>(R.id.textInputDescription).text.toString()

            val intent = Intent()
            intent.putExtra("title", title)
            intent.putExtra("description", description)

            setResult(RESULT_OK, intent)
            finish()
        }
    }


}