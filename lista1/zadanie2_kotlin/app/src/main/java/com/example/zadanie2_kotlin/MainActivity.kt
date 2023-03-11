package com.example.zadanie2_kotlin

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    private var playerScore = 0
    private var computerScore = 0
    private lateinit var scoreTextView: TextView
    private lateinit var playerChoiceImageView: ImageView
    private lateinit var computerChoiceImageView: ImageView

    internal enum class Choice {
        ROCK, PAPER, SCISSORS
    }

    private val randomChoice: Choice
        get() {
            return when (Random.nextInt(3)) {
                0 -> {
                    computerChoiceImageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.rock
                        )
                    )
                    Choice.ROCK
                }
                1 -> {
                    computerChoiceImageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.paper
                        )
                    )
                    Choice.PAPER
                }
                else -> {
                    computerChoiceImageView.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.scissors
                        )
                    )
                    Choice.SCISSORS
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.score_text)
        playerChoiceImageView = findViewById(R.id.your_choice)
        computerChoiceImageView = findViewById(R.id.computer_choice)
    }

    fun rockButtonClicked(view: View?) {
        playerChoiceImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.rock
            )
        )
        when (randomChoice) {
            Choice.ROCK -> {
                // draw
                Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
            }
            Choice.PAPER -> {
                // computer wins
                computerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore
                    )

                Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // player wins
                playerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore

                    )
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun paperButtonClicked(view: View?) {
        playerChoiceImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.paper
            )
        )
        when (randomChoice) {
            Choice.ROCK -> {
                // player wins
                playerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore
                    )
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
            }
            Choice.PAPER -> {
                // draw
                Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // computer wins
                computerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore
                    )

                Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun scissorsButtonClicked(view: View?) {
        playerChoiceImageView.setImageDrawable(
            AppCompatResources.getDrawable(
                this,
                R.drawable.scissors
            )
        )
        when (randomChoice) {
            Choice.ROCK -> {
                // computer wins
                computerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore
                    )
                Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show()
            }
            Choice.PAPER -> {
                // player wins
                playerScore++
                scoreTextView.text =
                    String.format(
                        "score: %d-%d",
                        playerScore,
                        computerScore

                    )
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // draw
                Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}