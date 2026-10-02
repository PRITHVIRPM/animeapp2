package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class WatchStatus(val label: String) {
    PLAN_TO_WATCH("Plan to Watch"),
    WATCHING("Watching"),
    COMPLETED("Completed"),
    FAVORITE("Favorite")
}

@Entity(tableName = "watchlist")
data class WatchlistEntity(
    @PrimaryKey val animeId: Int,
    val title: String,
    val japaneseTitle: String = "",
    val coverUrl: String,
    val score: Double,
    val totalEpisodes: Int,
    val watchedEpisodes: Int = 0,
    val status: String = WatchStatus.PLAN_TO_WATCH.name,
    val userRating: Int = 0, // 0-10
    val userNotes: String = "",
    val isFavorite: Boolean = false,
    val addedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
