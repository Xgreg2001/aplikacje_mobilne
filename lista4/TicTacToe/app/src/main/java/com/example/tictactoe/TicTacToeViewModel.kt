package com.example.tictactoe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TicTacToeViewModel(private val state: SavedStateHandle) : ViewModel() {
    val TAG = "TicTacToeViewModel"
    val boardSize = mutableStateOf(state["boardSize"] ?: 3);
    var gameBoard = mutableStateOf(state["gameBoard"] ?:arrayOfNulls<String>(boardSize.value * boardSize.value))
    var currentPlayer = state["currentPlayer"] ?: "X"
    val winner = mutableStateOf<String?>(state["winner"])
    val gameOver = mutableStateOf(state["gameOver"] ?:false)

    fun saveState() {
        state["boardSize"] = boardSize.value
        state["gameBoard"] = gameBoard.value
        state["currentPlayer"] = currentPlayer
        state["winner"] = winner.value
        state["gameOver"] = gameOver.value
    }

    fun resetGame() {
        gameBoard.value = arrayOfNulls(boardSize.value * boardSize.value)
        currentPlayer = "X"
        winner.value = null
        gameOver.value = false
    }

    fun makeMove(row: Int, col: Int) {
        Log.d(TAG, "makeMove: row: $row, col: $col")
        if (gameBoard.value[row * boardSize.value + col] == null && !gameOver.value) {
            val temp = gameBoard.value.copyOf()
            temp[row * boardSize.value + col] = currentPlayer
            gameBoard.value = temp
            if (checkWin(row, col)) {
                winner.value = currentPlayer
                gameOver.value = true
            } else {
                currentPlayer = if (currentPlayer == "X") "O" else "X"
            }
        }

        if (gameBoard.value.all { it != null }) {
            gameOver.value = true
            winner.value = null
        }
        saveState()
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        val playerSymbol = gameBoard.value[row * boardSize.value + col]

        // Check row
        var win = true
        for (i in 0 until boardSize.value) {
            if (gameBoard.value[row * boardSize.value + i] != playerSymbol) {
                win = false
                break
            }
        }
        if (win) return true

        // Check column
        win = true
        for (i in 0 until boardSize.value) {
            if (gameBoard.value[i * boardSize.value + col] != playerSymbol) {
                win = false
                break
            }
        }
        if (win) return true

        // Check diagonal (top-left to bottom-right)
        if (row == col) {
            win = true
            for (i in 0 until boardSize.value) {
                if (gameBoard.value[i * boardSize.value + i] != playerSymbol) {
                    win = false
                    break
                }
            }
            if (win) return true
        }

        // Check diagonal (top-right to bottom-left)
        if (row + col == boardSize.value - 1) {
            win = true
            for (i in 0 until boardSize.value) {
                if (gameBoard.value[i * boardSize.value + boardSize.value - 1 - i] != playerSymbol) {
                    win = false
                    break
                }
            }
            if (win) return true
        }

        return false
    }

    fun setBoardSize(size: Int) {
        boardSize.value = size
        resetGame()
        saveState()
    }
}