package com.example.hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private val usedLetters = HashSet<Char>()
    private var currentWord = ""
    private var mistakes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val textInput = findViewById<TextView>(R.id.textInputEditText)
        val words = resources.getStringArray(R.array.dictionary)
        val wordTextView = findViewById<TextView>(R.id.word)
        val imageView = findViewById<ImageView>(R.id.imageView)

        resetGame(button, textInput, words, wordTextView, imageView)
    }

    private fun take_input(button: Button, textInput: TextView, words: Array<String>, wordTextView: TextView, imageView: ImageView){

        var input = textInput.text.toString()
        textInput.text = ""

        if (input.length != 1) {

            Toast.makeText(this, "Please enter a single letter", Toast.LENGTH_SHORT).show()
            return
        }

        input = input.lowercase()

        if (usedLetters.contains(input[0])) {
            Toast.makeText(this, "You already used this letter", Toast.LENGTH_SHORT).show()
            return
        }

        usedLetters.add(input[0])

        if (currentWord.contains(input[0])) {
            for (i in currentWord.indices) {
                if (currentWord[i] == input[0]) {
                    wordTextView.text = wordTextView.text.toString().replaceRange(i, i + 1, input)
                }
            }
            if (!wordTextView.text.contains("?")) {
                Toast.makeText(this, "You won", Toast.LENGTH_SHORT).show()
                button.text = getString(R.string.button_play_again)
                button.setOnClickListener() {
                    resetGame(button, textInput, words, wordTextView, imageView)
                    button.text = getString(R.string.button_guess)
                }
            }
        } else {
            mistakes++
            imageView.setImageResource(resources.getIdentifier("hangman$mistakes", "drawable", packageName))
            if (mistakes == 10){
                Toast.makeText(this, "You lost", Toast.LENGTH_SHORT).show()
                button.text = getString(R.string.button_play_again)
                button.setOnClickListener() {
                    resetGame(button, textInput, words, wordTextView, imageView)
                    button.text = getString(R.string.button_guess)
                }
            }
        }
    }

    fun resetGame(button: Button, textInput: TextView, words: Array<String>, wordTextView: TextView, imageView: ImageView) {
        mistakes = 0
        usedLetters.clear()
        currentWord = words.random()
        wordTextView.text = "?".repeat(currentWord.length)
        imageView.setImageResource(R.drawable.hangman0)
        textInput.text = ""

        button.setOnClickListener() {
            take_input(button, textInput, words, wordTextView, imageView)
        }
    }


}