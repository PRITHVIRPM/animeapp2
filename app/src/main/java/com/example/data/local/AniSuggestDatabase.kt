package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [WatchlistEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AniSuggestDatabase : RoomDatabase() {

    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: AniSuggestDatabase? = null

        fun getInstance(context: Context): AniSuggestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AniSuggestDatabase::class.java,
                    "anisuggest_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
