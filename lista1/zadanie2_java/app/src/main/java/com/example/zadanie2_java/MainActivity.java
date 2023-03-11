package com.example.zadanie2_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private int playerScore = 0;
    private int computerScore = 0;
    private TextView scoreTextView;
    private ImageView playerChoiceImageView;
    private ImageView computerChoiceImageView;

    enum Choice {
        ROCK,
        PAPER,
        SCISSORS
    }

    Choice getRandomChoice() {
        Random random = new Random();
        int choice = random.nextInt(3);
        if (choice == 0) {
            computerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.rock));
            return Choice.ROCK;
        } else if (choice == 1) {
            computerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.paper));
            return Choice.PAPER;
        } else {
            computerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.scissors));
            return Choice.SCISSORS;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.score_text);
        playerChoiceImageView = findViewById(R.id.your_choice);
        computerChoiceImageView = findViewById(R.id.computer_choice);
    }

    public void rockButtonClicked(View view) {
        playerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.rock));

        Choice computerChoice = getRandomChoice();
        if (computerChoice == Choice.ROCK) {
            // draw
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        } else if (computerChoice == Choice.PAPER) {
            // computer wins
            computerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show();
        } else {
            // player wins
            playerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
        }
    }

    public void paperButtonClicked(View view) {
        playerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.paper));

        Choice computerChoice = getRandomChoice();
        if (computerChoice == Choice.ROCK) {
            // player wins
            playerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
        } else if (computerChoice == Choice.PAPER) {
            // draw
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        } else {
            // computer wins
            computerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show();
        }
    }

    public void scissorsButtonClicked(View view) {
        playerChoiceImageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.scissors));

        Choice computerChoice = getRandomChoice();
        if (computerChoice == Choice.ROCK) {
            // computer wins
            computerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "Computer wins!", Toast.LENGTH_SHORT).show();
        } else if (computerChoice == Choice.PAPER) {
            // player wins
            playerScore++;
            scoreTextView.setText(String.format(Locale.getDefault(), "score: %d-%d", playerScore, computerScore));
            Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
        } else {
            // draw
            Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        }
    }
}