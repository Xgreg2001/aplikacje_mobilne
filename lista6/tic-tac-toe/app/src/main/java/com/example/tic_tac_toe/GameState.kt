package com.example.tic_tac_toe

data class GameState(
    var player1Id: String? = null,
    var player2Id: String? = null,
    var board: Array<IntArray>? = null,
    var currentPlayerId: String? = null
) {
    init {
        this.board = Array(3) { IntArray(3) }
        this.currentPlayerId = player1Id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (player1Id != other.player1Id) return false
        if (player2Id != other.player2Id) return false
        if (board != null) {
            if (other.board == null) return false
            if (!board.contentDeepEquals(other.board)) return false
        } else if (other.board != null) return false
        if (currentPlayerId != other.currentPlayerId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = player1Id?.hashCode() ?: 0
        result = 31 * result + (player2Id?.hashCode() ?: 0)
        result = 31 * result + (board?.contentDeepHashCode() ?: 0)
        result = 31 * result + (currentPlayerId?.hashCode() ?: 0)
        return result
    }
}