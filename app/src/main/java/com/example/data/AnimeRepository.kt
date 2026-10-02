package com.example.data

import com.example.data.api.JikanApiClient
import com.example.data.api.toAnime
import com.example.data.local.WatchStatus
import com.example.data.local.WatchlistDao
import com.example.data.local.WatchlistEntity
import com.example.model.Anime
import com.example.model.AnimeFormat
import com.example.model.AnimeMood
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

data class QuizAnswers(
    val mood: AnimeMood?,
    val formatPreference: String, // "ANY", "SHORT", "LONG", "MOVIE"
    val pacingTone: String // "FAST_HYPE", "DEEP_EMOTIONAL", "COZY_COMEDY"
)

data class UserPreferenceFilter(
    val selectedMoods: Set<AnimeMood> = emptySet(),
    val selectedGenres: Set<String> = emptySet(),
    val lengthCategory: String = "ANY", // "ANY", "QUICK", "STANDARD", "EPIC", "MOVIE"
    val minScore: Double = 0.0,
    val releaseEra: String = "ALL" // "ALL", "MODERN", "RECENT", "CLASSIC"
)

data class ScoredAnimeSuggestion(
    val anime: Anime,
    val matchPercentage: Int,
    val matchReasons: List<String>
)

class AnimeRepository(
    private val watchlistDao: WatchlistDao
) {

    fun getAllAnime(): List<Anime> = AnimeCatalog.allAnime

    fun getSpotlight(): Anime = AnimeCatalog.getFeaturedSpotlight()

    fun getAnimeById(id: Int): Anime? = AnimeCatalog.getAnimeById(id)

    fun getByMood(mood: AnimeMood): List<Anime> = AnimeCatalog.getByMood(mood)

    fun getByGenre(genre: String): List<Anime> = AnimeCatalog.getByGenre(genre)

    fun searchAnime(query: String): List<Anime> = AnimeCatalog.search(query)

    suspend fun searchLiveJikan(query: String): Result<List<Anime>> {
        return try {
            val response = JikanApiClient.apiService.searchAnime(query = query.trim())
            val list = response.data.map { it.toAnime() }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopAiringJikan(): Result<List<Anime>> {
        return try {
            val response = JikanApiClient.apiService.getTopAnime()
            val list = response.data.map { it.toAnime() }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSeasonalJikan(): Result<List<Anime>> {
        return try {
            val response = JikanApiClient.apiService.getSeasonalAnime()
            val list = response.data.map { it.toAnime() }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSimilarAnime(anime: Anime): List<Anime> = AnimeCatalog.getSimilarAnime(anime)

    fun getRandomSuggestion(
        filterMood: AnimeMood? = null,
        minScore: Double = 8.0,
        preferredFormat: AnimeFormat? = null
    ): Anime {
        var pool = AnimeCatalog.allAnime.filter { it.score >= minScore }
        if (filterMood != null) {
            val moodMatches = pool.filter { it.vibes.contains(filterMood) }
            if (moodMatches.isNotEmpty()) {
                pool = moodMatches
            }
        }
        if (preferredFormat != null) {
            val formatMatches = pool.filter { it.format == preferredFormat }
            if (formatMatches.isNotEmpty()) {
                pool = formatMatches
            }
        }
        return pool.randomOrNull() ?: AnimeCatalog.allAnime.random()
    }

    fun getQuizRecommendations(answers: QuizAnswers): List<Anime> {
        val scoredList = AnimeCatalog.allAnime.map { anime ->
            var points = 0

            // Mood match
            if (answers.mood != null && anime.vibes.contains(answers.mood)) {
                points += 15
            }

            // Format match
            when (answers.formatPreference) {
                "MOVIE" -> if (anime.format == AnimeFormat.MOVIE) points += 12
                "SHORT" -> if (anime.episodes in 10..13) points += 10
                "LONG" -> if (anime.episodes >= 24) points += 10
            }

            // Pacing & tone
            when (answers.pacingTone) {
                "FAST_HYPE" -> if (anime.vibes.contains(AnimeMood.HYPE) || anime.vibes.contains(AnimeMood.DARK)) points += 8
                "DEEP_EMOTIONAL" -> if (anime.vibes.contains(AnimeMood.TEARJERKER) || anime.vibes.contains(AnimeMood.PSYCHOLOGICAL)) points += 8
                "COZY_COMEDY" -> if (anime.vibes.contains(AnimeMood.COZY) || anime.vibes.contains(AnimeMood.COMEDY) || anime.vibes.contains(AnimeMood.ROMANCE)) points += 8
            }

            // Weight by rating
            points += (anime.score * 2).toInt()

            Pair(anime, points)
        }

        return scoredList.sortedByDescending { it.second }.take(4).map { it.first }
    }

    fun getCustomPreferenceSuggestions(filter: UserPreferenceFilter): List<ScoredAnimeSuggestion> {
        val candidates = AnimeCatalog.allAnime.filter { anime ->
            if (filter.minScore > 0.0 && anime.score < filter.minScore) return@filter false
            true
        }

        val scored = candidates.map { anime ->
            var rawScore = 50 // Base baseline
            val reasons = mutableListOf<String>()

            // 1. Mood matches
            if (filter.selectedMoods.isNotEmpty()) {
                val matchedMoods = anime.vibes.intersect(filter.selectedMoods)
                if (matchedMoods.isNotEmpty()) {
                    rawScore += matchedMoods.size * 18
                    reasons.add("Fits your ${matchedMoods.joinToString { it.title }} vibe")
                }
            }

            // 2. Genre matches
            if (filter.selectedGenres.isNotEmpty()) {
                val matchedGenres = anime.genres.filter { g ->
                    filter.selectedGenres.any { fg -> fg.equals(g, ignoreCase = true) }
                }
                if (matchedGenres.isNotEmpty()) {
                    rawScore += matchedGenres.size * 14
                    reasons.add("Matches genres: ${matchedGenres.joinToString(", ")}")
                }
            }

            // 3. Length / Format preference
            when (filter.lengthCategory) {
                "QUICK" -> {
                    if (anime.episodes in 10..13) {
                        rawScore += 16
                        reasons.add("Ideal quick binge: ${anime.episodes} episodes")
                    } else if (anime.episodes in 14..24) {
                        rawScore += 6
                    }
                }
                "STANDARD" -> {
                    if (anime.episodes in 20..30) {
                        rawScore += 16
                        reasons.add("Standard season pacing: ${anime.episodes} episodes")
                    }
                }
                "EPIC" -> {
                    if (anime.episodes >= 36) {
                        rawScore += 18
                        reasons.add("Deep epic world with ${anime.episodes} episodes")
                    }
                }
                "MOVIE" -> {
                    if (anime.format == AnimeFormat.MOVIE) {
                        rawScore += 22
                        reasons.add("Feature-length movie (${anime.duration})")
                    }
                }
            }

            // 4. Release Era
            when (filter.releaseEra) {
                "MODERN" -> {
                    if (anime.year >= 2020) {
                        rawScore += 12
                        reasons.add("Modern peak animation (${anime.year})")
                    }
                }
                "RECENT" -> {
                    if (anime.year in 2015..2019) {
                        rawScore += 12
                        reasons.add("Recent acclaimed release (${anime.year})")
                    }
                }
                "CLASSIC" -> {
                    if (anime.year <= 2014) {
                        rawScore += 12
                        reasons.add("Beloved classic era (${anime.year})")
                    }
                }
            }

            // 5. Rating weight
            rawScore += ((anime.score - 8.0) * 10).toInt().coerceAtLeast(0)
            if (anime.score >= 9.0) {
                reasons.add("Elite ${anime.score} MAL Masterpiece rating")
            }

            if (reasons.isEmpty()) {
                reasons.add("Top critically acclaimed recommendation by ${anime.studio}")
            }

            val percentage = (rawScore.coerceIn(75, 99))
            ScoredAnimeSuggestion(
                anime = anime,
                matchPercentage = percentage,
                matchReasons = reasons
            )
        }

        return scored.sortedByDescending { it.matchPercentage }
    }

    // Watchlist operations
    fun getAllWatchlist(): Flow<List<WatchlistEntity>> = watchlistDao.getAllWatchlist()

    fun getFavorites(): Flow<List<WatchlistEntity>> = watchlistDao.getFavorites()

    fun getWatchlistByStatus(status: WatchStatus): Flow<List<WatchlistEntity>> =
        watchlistDao.getByStatus(status.name)

    fun getWatchlistEntry(animeId: Int): Flow<WatchlistEntity?> =
        watchlistDao.getEntryForAnime(animeId)

    suspend fun setWatchStatus(anime: Anime, status: WatchStatus) {
        val existing = watchlistDao.getEntryByIdDirect(anime.id)
        if (existing == null) {
            watchlistDao.upsert(
                WatchlistEntity(
                    animeId = anime.id,
                    title = anime.title,
                    japaneseTitle = anime.japaneseTitle,
                    coverUrl = anime.coverUrl,
                    score = anime.score,
                    totalEpisodes = anime.episodes,
                    watchedEpisodes = if (status == WatchStatus.COMPLETED) anime.episodes else 0,
                    status = status.name,
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            watchlistDao.update(
                existing.copy(
                    status = status.name,
                    watchedEpisodes = if (status == WatchStatus.COMPLETED) anime.episodes else existing.watchedEpisodes,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun toggleFavorite(anime: Anime) {
        val existing = watchlistDao.getEntryByIdDirect(anime.id)
        if (existing == null) {
            watchlistDao.upsert(
                WatchlistEntity(
                    animeId = anime.id,
                    title = anime.title,
                    japaneseTitle = anime.japaneseTitle,
                    coverUrl = anime.coverUrl,
                    score = anime.score,
                    totalEpisodes = anime.episodes,
                    isFavorite = true,
                    status = WatchStatus.FAVORITE.name,
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            val newFav = !existing.isFavorite
            watchlistDao.update(
                existing.copy(
                    isFavorite = newFav,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun updateEpisodeProgress(animeId: Int, watched: Int) {
        watchlistDao.updateEpisodesWatched(animeId, watched)
    }

    suspend fun removeFromWatchlist(animeId: Int) {
        watchlistDao.deleteById(animeId)
    }
}
