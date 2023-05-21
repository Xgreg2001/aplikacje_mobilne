package com.example.spaceinvaders

import android.os.Bundle
import android.text.InputType
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.spaceinvaders.storage.GameData
import com.example.spaceinvaders.storage.GameDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var gameSurfaceView: GameSurfaceView
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var gameDatabase: GameDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameSurfaceView = GameSurfaceView(this)
        setContentView(gameSurfaceView)

        gameDatabase = GameDatabase.getDatabase(this)

        // Hide both the navigation bar and the status bar.
        window.insetsController?.let {
            it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

     fun showEndGameDialog(score: Int) {
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Your score: $score. Enter your username to submit your score:")
            .setView(editText)
            .setPositiveButton("Submit") { _, _ ->
                val username = editText.text.toString()
                scope.launch {
                    gameDatabase.gameDao().insert(GameData(score = score, username = username))
                }
                gameDatabase.gameDao().getAllDataSortedByScore().observe(this) {
                    val scores = it.map { gameData -> "${gameData.username}: ${gameData.score}" }
                    val scoresString = scores.joinToString("\n")
                    AlertDialog.Builder(this)
                        .setTitle("Scores")
                        .setMessage(scoresString)
                        .setPositiveButton("OK") {
                                _, _ -> gameSurfaceView.reset()
                        }
                        .show()
                }

            }
            .setNegativeButton("Cancel") {
                    _, _ -> gameSurfaceView.reset()
            }
            .show()
    }

}