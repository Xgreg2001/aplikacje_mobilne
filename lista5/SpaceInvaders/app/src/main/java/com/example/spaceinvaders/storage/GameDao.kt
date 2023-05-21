package com.example.spaceinvaders.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameData: GameData)
    @Query("SELECT * FROM game_data_table ORDER BY id DESC")
    fun getAllData(): LiveData<List<GameData>>

    @Query("SELECT * FROM game_data_table WHERE username = :username")
    fun getUserData(username: String): LiveData<List<GameData>>

    @Query("SELECT * FROM game_data_table ORDER BY score DESC")
    fun getAllDataSortedByScore(): LiveData<List<GameData>>
}