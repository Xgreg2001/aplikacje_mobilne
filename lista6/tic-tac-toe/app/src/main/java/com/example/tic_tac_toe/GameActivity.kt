package com.example.tic_tac_toe

import android.os.Bundle
import android.view.View
import android.widget.Button
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

        database = FirebaseDatabase.getInstance()
        val gameRef = database.getReference("games").child(gameId)

        gameRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                gameState = dataSnapshot.getValue(GameState::class.java)!!
                updateUI()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
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
            if (gameState.board!![i][j] == 0 && gameState.currentPlayerId  == myUserId) {
                gameState.board!![i][j] = if (gameState.player1Id == myUserId) 1 else 2
                gameState.currentPlayerId = if (gameState.currentPlayerId == gameState.player1Id) gameState.player2Id else gameState.player1Id
                database.getReference("games").child(gameId).setValue(gameState)
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
}

