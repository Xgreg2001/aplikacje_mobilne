package com.example.zadanie1_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int r1 = 0;
    private int r2 = 0;
    private int score = 0;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        roll();
    }

    private void roll() {
        ((TextView) findViewById(R.id.points)).setText("Score: " + score);
        r1 = random.nextInt(10);
        r2 = random.nextInt(10);
        ((Button) findViewById(R.id.leftClick)).setText("" + r1);
        ((Button) findViewById(R.id.rightClick)).setText("" + r2);
    }

    public void buttonLeft(View view) {
        if (r1 >= r2) {
            score++;
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show();
        } else {
            score--;
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show();
        }
        roll();
    }

    public void buttonRight(View view) {
        if (r1 <= r2) {
            score++;
            Toast.makeText(this, "Dobrze!!!", Toast.LENGTH_SHORT).show();
        } else {
            score--;
            Toast.makeText(this, "Źle!!!", Toast.LENGTH_SHORT).show();
        }
        roll();
    }
}