package com.example.tic_tac_toe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GameActivity : AppCompatActivity() {
    private lateinit var gameState: GameState
    private lateinit var gameId: String
    private lateinit var myUserId: String
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        myUserId = FirebaseAuth.getInstance().currentUser!!.uid
        gameId = intent.getStringExtra("GAME_ID")!!

        database = FirebaseDatabase.getInstance("https://tic-tac-toe-cb54d-default-rtdb.europe-west1.firebasedatabase.app")

        val gameRef = database.getReference("games").child(gameId)

        gameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gameState = dataSnapshot.getValue(GameState::class.java)!!

                if (gameState.status != GameStatus.IN_PROGRESS) {
                    makeGameEndToast()
                    finish()
                }

                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    fun makeGameEndToast() {
        if (gameState.status == GameStatus.PLAYER_1_WON) {
            if (gameState.player1Id == myUserId) {
                Toast.makeText(this@GameActivity, "You won!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@GameActivity, "You lost!", Toast.LENGTH_LONG).show()
            }
        } else if (gameState.status == GameStatus.PLAYER_2_WON) {
            if (gameState.player2Id == myUserId) {
                Toast.makeText(this@GameActivity, "You won!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@GameActivity, "You lost!", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this@GameActivity, "It's a draw!", Toast.LENGTH_LONG).show()
        }

    }

    private fun updateUI() {
        for (i in 0..2) {
            for (j in 0..2) {
                val button = findViewById<Button>(getButtonId(i, j))
                when (gameState.board!![i][j]) {
                    0 -> button.text = ""
                    1 -> button.text = "X"
                    2 -> button.text = "O"
                }
            }
        }
    }

    private fun getButtonId(i: Int, j: Int): Int {
        return resources.getIdentifier("button_${i}_${j}", "id", packageName)
    }

    fun onButtonClick(view: View) {
        val buttonId = view.id
        val ij = getBoardPosition(buttonId)
        if (ij != null) {
            val (i, j) = ij
            if (gameState.board!![i][j] == 0 && gameState.currentPlayerId == myUserId) {
                gameState.board!![i][j] = if (gameState.player1Id == myUserId) 1 else 2
                gameState.currentPlayerId = if (gameState.currentPlayerId == gameState.player1Id) gameState.player2Id else gameState.player1Id
                val gameRef = database.getReference("games").child(gameId)
                if (checkGameOver()) {
                    val usersRef = database.getReference("users")
                    usersRef.child(gameState.player1Id!!).child("currentGame").setValue(null)
                    usersRef.child(gameState.player2Id!!).child("currentGame").setValue(null)
                }
                gameRef.setValue(gameState)
                if (gameState.status != GameStatus.IN_PROGRESS) {
                    makeGameEndToast()
                    finish()
                }
            }
        }
    }

    private fun getBoardPosition(buttonId: Int): Pair<Int, Int>? {
        for (i in 0..2) {
            for (j in 0..2) {
                if (getButtonId(i, j) == buttonId) {
                    return Pair(i, j)
                }
            }
        }
        return null
    }

    private fun checkGameOver(): Boolean {
        // Check rows
        for (i in 0..2) {
            if (gameState.board!![i][0] == gameState.board!![i][1] &&
                gameState.board!![i][1] == gameState.board!![i][2] &&
                gameState.board!![i][0] != 0
            ) {

                gameState.status = if (gameState.board!![i][0] == 1) GameStatus.PLAYER_1_WON else GameStatus.PLAYER_2_WON
                return true
            }
        }

        // Check columns
        for (j in 0..2) {
            if (gameState.board!![0][j] == gameState.board!![1][j] &&
                gameState.board!![1][j] == gameState.board!![2][j] &&
                gameState.board!![0][j] != 0
            ) {
                gameState.status = if (gameState.board!![0][j] == 1) GameStatus.PLAYER_1_WON else GameStatus.PLAYER_2_WON
                return true
            }
        }

        // Check diagonals
        if (gameState.board!![0][0] == gameState.board!![1][1] &&
            gameState.board!![1][1] == gameState.board!![2][2] &&
            gameState.board!![0][0] != 0
        ) {
            gameState.status = if (gameState.board!![0][0] == 1) GameStatus.PLAYER_1_WON else GameStatus.PLAYER_2_WON
            return true
        }
        if (gameState.board!![0][2] == gameState.board!![1][1] &&
            gameState.board!![1][1] == gameState.board!![2][0] &&
            gameState.board!![0][2] != 0
        ) {
            gameState.status = if (gameState.board!![0][2] == 1) GameStatus.PLAYER_1_WON else GameStatus.PLAYER_2_WON
            return true
        }

        // Check rows, columns, and diagonals...
        // Check if board is full
        for (i in 0..2) {
            for (j in 0..2) {
                if (gameState.board!![i][j] == 0) {
                    return false
                }
            }
        }

        gameState.status = GameStatus.DRAW
        return true
    }

}

