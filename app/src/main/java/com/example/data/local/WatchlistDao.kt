package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM watchlist ORDER BY updatedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavorites(): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE status = :status ORDER BY updatedAt DESC")
    fun getByStatus(status: String): Flow<List<WatchlistEntity>>

    @Query("SELECT * FROM watchlist WHERE animeId = :animeId LIMIT 1")
    fun getEntryForAnime(animeId: Int): Flow<WatchlistEntity?>

    @Query("SELECT * FROM watchlist WHERE animeId = :animeId LIMIT 1")
    suspend fun getEntryByIdDirect(animeId: Int): WatchlistEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WatchlistEntity)

    @Update
    suspend fun update(entry: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE animeId = :animeId")
    suspend fun deleteById(animeId: Int)

    @Query("UPDATE watchlist SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE animeId = :animeId")
    suspend fun updateFavorite(animeId: Int, isFavorite: Boolean, updatedAt: Long = System.currentTimeMillis())

    @Query("UPDATE watchlist SET watchedEpisodes = :watched, updatedAt = :updatedAt WHERE animeId = :animeId")
    suspend fun updateEpisodesWatched(animeId: Int, watched: Int, updatedAt: Long = System.currentTimeMillis())
}
