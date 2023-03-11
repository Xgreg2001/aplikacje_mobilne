package com.example.zadanie1_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var score = 0
    private var r1 = 0
    private var r2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        roll()
    }

    fun roll(){
        findViewById<TextView>(R.id.points).text = "Punkty: " + score
        r1 = Random.nextInt(10)
        r2 = Random.nextInt(10)
        findViewById<TextView>(R.id.leftClick).text = r1.toString()
        findViewById<TextView>(R.id.rightClick).text = r2.toString()

    }

    fun buttonRight(view: View) {
        if (r2 >= r1){
            score++
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show()
        }
        else{
            score--
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show()
        }
        roll()
    }

    fun buttonLeft(view: View) {
        if (r1 >= r2){
            score++
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show()
        }
        else{
            score--
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show()
        }
        roll()
    }
}