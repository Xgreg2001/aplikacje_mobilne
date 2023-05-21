package com.example.spaceinvaders.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_data_table")
data class GameData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "score")
    val score: Int,

    @ColumnInfo(name = "username")
    val username: String
)