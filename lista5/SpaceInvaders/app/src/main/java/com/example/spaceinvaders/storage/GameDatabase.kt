package com.example.spaceinvaders.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameData::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object{
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}