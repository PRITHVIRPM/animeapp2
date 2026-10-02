package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AnimeCatalog
import com.example.data.AnimeRepository
import com.example.data.QuizAnswers
import com.example.data.ScoredAnimeSuggestion
import com.example.data.UserPreferenceFilter
import com.example.data.local.AniSuggestDatabase
import com.example.data.local.WatchStatus
import com.example.data.local.WatchlistEntity
import com.example.model.Anime
import com.example.model.AnimeFormat
import com.example.model.AnimeMood
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScreenTab(val title: String, val iconName: String) {
    object Discover : ScreenTab("Discover", "Explore")
    object Suggest : ScreenTab("Suggest", "Tune")
    object Surprise : ScreenTab("Surprise", "Casino")
    object Matcher : ScreenTab("Similar", "Hub")
    object Watchlist : ScreenTab("Watchlist", "Bookmarks")
}

data class QuizState(
    val isOpen: Boolean = false,
    val step: Int = 1, // 1 to 3
    val selectedMood: AnimeMood? = null,
    val selectedFormat: String = "ANY",
    val selectedPacing: String = "FAST_HYPE",
    val recommendations: List<Anime> = emptyList()
)

data class UiState(
    val currentTab: ScreenTab = ScreenTab.Discover,
    val selectedMood: AnimeMood? = null,
    val selectedGenre: String? = null,
    val searchQuery: String = "",
    val filteredAnimeList: List<Anime> = emptyList(),
    val selectedAnimeForDetail: Anime? = null,
    // Surprise wheel state
    val surpriseAnime: Anime? = null,
    val isSpinning: Boolean = false,
    val surpriseMoodFilter: AnimeMood? = null,
    val surpriseFormatFilter: AnimeFormat? = null,
    // Similar matcher state
    val matcherBaseAnime: Anime? = null,
    val similarResults: List<Anime> = emptyList(),
    // Custom Preference Matcher state
    val userPreferenceFilter: UserPreferenceFilter = UserPreferenceFilter(),
    val preferenceSuggestions: List<ScoredAnimeSuggestion> = emptyList(),
    val hasGeneratedSuggestions: Boolean = false,
    // Live Jikan API state
    val isLiveJikanMode: Boolean = false,
    val isJikanLoading: Boolean = false,
    val jikanErrorMessage: String? = null,
    val liveJikanResults: List<Anime> = emptyList(),
    val jikanSourceTab: String = "SEARCH", // "SEARCH", "TOP_AIRING", "SEASONAL"
    // Quiz state
    val quizState: QuizState = QuizState()
)

class AnimeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AnimeRepository

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val watchlistItems: StateFlow<List<WatchlistEntity>>

    private var searchDebounceJob: Job? = null

    init {
        val db = AniSuggestDatabase.getInstance(application)
        repository = AnimeRepository(db.watchlistDao())

        val all = repository.getAllAnime()
        val initialSuggestions = repository.getCustomPreferenceSuggestions(UserPreferenceFilter())

        _uiState.value = _uiState.value.copy(
            filteredAnimeList = all,
            surpriseAnime = all.firstOrNull(),
            matcherBaseAnime = all.find { it.title.contains("Death Note") } ?: all.firstOrNull(),
            similarResults = repository.getSimilarAnime(all.find { it.title.contains("Death Note") } ?: all.first()),
            preferenceSuggestions = initialSuggestions
        )

        watchlistItems = repository.getAllWatchlist().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun selectTab(tab: ScreenTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun openAnimeDetail(anime: Anime) {
        _uiState.value = _uiState.value.copy(selectedAnimeForDetail = anime)
    }

    fun closeAnimeDetail() {
        _uiState.value = _uiState.value.copy(selectedAnimeForDetail = null)
    }

    fun selectMoodFilter(mood: AnimeMood?) {
        val newMood = if (_uiState.value.selectedMood == mood) null else mood
        _uiState.value = _uiState.value.copy(selectedMood = newMood)
        applyFilters()
    }

    fun selectGenreFilter(genre: String?) {
        val newGenre = if (_uiState.value.selectedGenre == genre) null else genre
        _uiState.value = _uiState.value.copy(selectedGenre = newGenre)
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (_uiState.value.isLiveJikanMode) {
            searchDebounceJob?.cancel()
            val trimmed = query.trim()
            if (trimmed.length >= 2) {
                searchDebounceJob = viewModelScope.launch {
                    delay(400)
                    executeLiveSearch(trimmed)
                }
            } else if (trimmed.isEmpty()) {
                fetchTopAiringJikan()
            }
        } else {
            applyFilters()
        }
    }

    private fun applyFilters() {
        val state = _uiState.value
        var list = AnimeCatalog.allAnime

        if (state.searchQuery.isNotBlank()) {
            list = repository.searchAnime(state.searchQuery)
        }
        if (state.selectedMood != null) {
            list = list.filter { it.vibes.contains(state.selectedMood) }
        }
        if (state.selectedGenre != null) {
            list = list.filter { it.genres.any { g -> g.equals(state.selectedGenre, ignoreCase = true) } }
        }

        _uiState.value = _uiState.value.copy(filteredAnimeList = list)
    }

    // Jikan Live API Methods
    fun toggleLiveJikanMode(enable: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLiveJikanMode = enable,
            jikanErrorMessage = null
        )
        if (enable) {
            if (_uiState.value.liveJikanResults.isEmpty()) {
                if (_uiState.value.searchQuery.isNotBlank()) {
                    executeLiveSearch(_uiState.value.searchQuery)
                } else {
                    fetchTopAiringJikan()
                }
            }
        } else {
            applyFilters()
        }
    }

    fun setJikanSourceTab(tab: String) {
        _uiState.value = _uiState.value.copy(jikanSourceTab = tab, jikanErrorMessage = null)
        when (tab) {
            "TOP_AIRING" -> fetchTopAiringJikan()
            "SEASONAL" -> fetchSeasonalJikan()
            "SEARCH" -> {
                if (_uiState.value.searchQuery.isNotBlank()) {
                    executeLiveSearch(_uiState.value.searchQuery)
                } else {
                    fetchTopAiringJikan()
                }
            }
        }
    }

    fun executeLiveSearch(query: String) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            fetchTopAiringJikan()
            return
        }
        _uiState.value = _uiState.value.copy(isJikanLoading = true, jikanErrorMessage = null)
        viewModelScope.launch {
            val result = repository.searchLiveJikan(trimmed)
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    liveJikanResults = list,
                    jikanErrorMessage = if (list.isEmpty()) "No online results found for \"$trimmed\"" else null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    jikanErrorMessage = "Live service error: ${error.localizedMessage ?: "Unable to connect"}. Tap to retry."
                )
            }
        }
    }

    fun fetchTopAiringJikan() {
        _uiState.value = _uiState.value.copy(isJikanLoading = true, jikanErrorMessage = null)
        viewModelScope.launch {
            val result = repository.getTopAiringJikan()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    liveJikanResults = list,
                    jikanErrorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    jikanErrorMessage = "Live server busy: ${error.localizedMessage ?: "Please tap retry"}."
                )
            }
        }
    }

    fun fetchSeasonalJikan() {
        _uiState.value = _uiState.value.copy(isJikanLoading = true, jikanErrorMessage = null)
        viewModelScope.launch {
            val result = repository.getSeasonalJikan()
            result.onSuccess { list ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    liveJikanResults = list,
                    jikanErrorMessage = null
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isJikanLoading = false,
                    jikanErrorMessage = "Live server busy: ${error.localizedMessage ?: "Please tap retry"}."
                )
            }
        }
    }

    // Custom Preference Suggestion Methods
    fun togglePreferenceMood(mood: AnimeMood) {
        val current = _uiState.value.userPreferenceFilter.selectedMoods
        val updated = if (current.contains(mood)) current - mood else current + mood
        val newFilter = _uiState.value.userPreferenceFilter.copy(selectedMoods = updated)
        _uiState.value = _uiState.value.copy(userPreferenceFilter = newFilter)
        updatePreferenceSuggestions(newFilter)
    }

    fun togglePreferenceGenre(genre: String) {
        val current = _uiState.value.userPreferenceFilter.selectedGenres
        val updated = if (current.contains(genre)) current - genre else current + genre
        val newFilter = _uiState.value.userPreferenceFilter.copy(selectedGenres = updated)
        _uiState.value = _uiState.value.copy(userPreferenceFilter = newFilter)
        updatePreferenceSuggestions(newFilter)
    }

    fun setPreferenceLength(length: String) {
        val newFilter = _uiState.value.userPreferenceFilter.copy(lengthCategory = length)
        _uiState.value = _uiState.value.copy(userPreferenceFilter = newFilter)
        updatePreferenceSuggestions(newFilter)
    }

    fun setPreferenceMinScore(score: Double) {
        val newFilter = _uiState.value.userPreferenceFilter.copy(minScore = score)
        _uiState.value = _uiState.value.copy(userPreferenceFilter = newFilter)
        updatePreferenceSuggestions(newFilter)
    }

    fun setPreferenceEra(era: String) {
        val newFilter = _uiState.value.userPreferenceFilter.copy(releaseEra = era)
        _uiState.value = _uiState.value.copy(userPreferenceFilter = newFilter)
        updatePreferenceSuggestions(newFilter)
    }

    fun resetPreferences() {
        val defaultFilter = UserPreferenceFilter()
        _uiState.value = _uiState.value.copy(
            userPreferenceFilter = defaultFilter,
            hasGeneratedSuggestions = false
        )
        updatePreferenceSuggestions(defaultFilter)
    }

    private fun updatePreferenceSuggestions(filter: UserPreferenceFilter) {
        val suggestions = repository.getCustomPreferenceSuggestions(filter)
        _uiState.value = _uiState.value.copy(
            preferenceSuggestions = suggestions,
            hasGeneratedSuggestions = true
        )
    }

    fun spinSurpriseWheel() {
        val state = _uiState.value
        _uiState.value = _uiState.value.copy(isSpinning = true)
        viewModelScope.launch {
            kotlinx.coroutines.delay(650)
            val picked = repository.getRandomSuggestion(
                filterMood = state.surpriseMoodFilter,
                minScore = 8.0,
                preferredFormat = state.surpriseFormatFilter
            )
            _uiState.value = _uiState.value.copy(
                surpriseAnime = picked,
                isSpinning = false
            )
        }
    }

    fun setSurpriseMoodFilter(mood: AnimeMood?) {
        val newMood = if (_uiState.value.surpriseMoodFilter == mood) null else mood
        _uiState.value = _uiState.value.copy(surpriseMoodFilter = newMood)
    }

    fun setSurpriseFormatFilter(format: AnimeFormat?) {
        val newFormat = if (_uiState.value.surpriseFormatFilter == format) null else format
        _uiState.value = _uiState.value.copy(surpriseFormatFilter = newFormat)
    }

    fun selectMatcherBaseAnime(anime: Anime) {
        val similar = repository.getSimilarAnime(anime)
        _uiState.value = _uiState.value.copy(
            matcherBaseAnime = anime,
            similarResults = similar
        )
    }

    fun startQuiz() {
        _uiState.value = _uiState.value.copy(
            quizState = QuizState(isOpen = true, step = 1)
        )
    }

    fun dismissQuiz() {
        _uiState.value = _uiState.value.copy(
            quizState = _uiState.value.quizState.copy(isOpen = false)
        )
    }

    fun setQuizMood(mood: AnimeMood) {
        _uiState.value = _uiState.value.copy(
            quizState = _uiState.value.quizState.copy(selectedMood = mood, step = 2)
        )
    }

    fun setQuizFormat(format: String) {
        _uiState.value = _uiState.value.copy(
            quizState = _uiState.value.quizState.copy(selectedFormat = format, step = 3)
        )
    }

    fun finishQuiz(pacing: String) {
        val currentQuiz = _uiState.value.quizState.copy(selectedPacing = pacing, step = 4)
        val recs = repository.getQuizRecommendations(
            QuizAnswers(
                mood = currentQuiz.selectedMood,
                formatPreference = currentQuiz.selectedFormat,
                pacingTone = pacing
            )
        )
        _uiState.value = _uiState.value.copy(
            quizState = currentQuiz.copy(recommendations = recs)
        )
    }

    fun addToWatchlist(anime: Anime, status: WatchStatus) {
        viewModelScope.launch {
            repository.setWatchStatus(anime, status)
        }
    }

    fun toggleFavorite(anime: Anime) {
        viewModelScope.launch {
            repository.toggleFavorite(anime)
        }
    }

    fun updateWatchedEpisodes(animeId: Int, newCount: Int) {
        viewModelScope.launch {
            repository.updateEpisodeProgress(animeId, newCount)
        }
    }

    fun removeFromWatchlist(animeId: Int) {
        viewModelScope.launch {
            repository.removeFromWatchlist(animeId)
        }
    }
}
