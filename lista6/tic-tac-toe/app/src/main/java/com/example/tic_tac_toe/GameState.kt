package com.example.tic_tac_toe

enum class GameStatus {
    IN_PROGRESS,
    PLAYER_1_WON,
    PLAYER_2_WON,
    DRAW
}

data class GameState(
    var player1Id: String? = null,
    var player2Id: String? = null,
    var board: MutableList<MutableList<Int>>? = null,
    var currentPlayerId: String? = null,
    var status: GameStatus = GameStatus.IN_PROGRESS
) {
    init {
        this.board = MutableList(3) { MutableList(3) { 0 } }
        this.currentPlayerId = player1Id
    }
}