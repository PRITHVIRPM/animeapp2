package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.WatchStatus
import com.example.ui.components.AnimeDetailSheet
import com.example.ui.components.QuizDialog
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.PreferenceSuggestionScreen
import com.example.ui.screens.SimilarMatcherScreen
import com.example.ui.screens.SurpriseScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkBg
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AnimeViewModel
import com.example.ui.viewmodel.ScreenTab
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AniSuggestApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AniSuggestApp(
    viewModel: AnimeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val watchlistItems by viewModel.watchlistItems.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle system back navigation
    BackHandler(enabled = uiState.selectedAnimeForDetail != null || uiState.quizState.isOpen || uiState.currentTab != ScreenTab.Discover) {
        when {
            uiState.selectedAnimeForDetail != null -> viewModel.closeAnimeDetail()
            uiState.quizState.isOpen -> viewModel.dismissQuiz()
            uiState.currentTab != ScreenTab.Discover -> viewModel.selectTab(ScreenTab.Discover)
        }
    }

    Scaffold(
        containerColor = AniDarkBg,
        bottomBar = {
            NavigationBar(
                containerColor = AniDarkSurface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                // Tab 1: Discover
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.Discover,
                    onClick = { viewModel.selectTab(ScreenTab.Discover) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == ScreenTab.Discover) Icons.Default.Explore else Icons.Outlined.Explore,
                            contentDescription = "Discover"
                        )
                    },
                    label = {
                        Text(
                            text = "Discover",
                            fontSize = 10.sp,
                            fontWeight = if (uiState.currentTab == ScreenTab.Discover) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AniCyanAccent,
                        indicatorColor = AniPurplePrimary,
                        unselectedIconColor = AniTextSecondary,
                        unselectedTextColor = AniTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_discover")
                )

                // Tab 2: Suggest (Custom Preference Matcher)
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.Suggest,
                    onClick = { viewModel.selectTab(ScreenTab.Suggest) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == ScreenTab.Suggest) Icons.Default.Tune else Icons.Outlined.Tune,
                            contentDescription = "Suggest"
                        )
                    },
                    label = {
                        Text(
                            text = "Suggest",
                            fontSize = 10.sp,
                            fontWeight = if (uiState.currentTab == ScreenTab.Suggest) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AniCyanAccent,
                        indicatorColor = AniPurplePrimary,
                        unselectedIconColor = AniTextSecondary,
                        unselectedTextColor = AniTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_suggest")
                )

                // Tab 3: Surprise Roulette
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.Surprise,
                    onClick = { viewModel.selectTab(ScreenTab.Surprise) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == ScreenTab.Surprise) Icons.Default.Casino else Icons.Outlined.Casino,
                            contentDescription = "Surprise"
                        )
                    },
                    label = {
                        Text(
                            text = "Roulette",
                            fontSize = 10.sp,
                            fontWeight = if (uiState.currentTab == ScreenTab.Surprise) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AniCyanAccent,
                        indicatorColor = AniPurplePrimary,
                        unselectedIconColor = AniTextSecondary,
                        unselectedTextColor = AniTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_surprise")
                )

                // Tab 4: Similar Matcher
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.Matcher,
                    onClick = { viewModel.selectTab(ScreenTab.Matcher) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == ScreenTab.Matcher) Icons.Default.AutoAwesome else Icons.Outlined.AutoAwesome,
                            contentDescription = "Similar"
                        )
                    },
                    label = {
                        Text(
                            text = "Similar",
                            fontSize = 10.sp,
                            fontWeight = if (uiState.currentTab == ScreenTab.Matcher) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AniCyanAccent,
                        indicatorColor = AniPurplePrimary,
                        unselectedIconColor = AniTextSecondary,
                        unselectedTextColor = AniTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_matcher")
                )

                // Tab 5: Watchlist
                NavigationBarItem(
                    selected = uiState.currentTab == ScreenTab.Watchlist,
                    onClick = { viewModel.selectTab(ScreenTab.Watchlist) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.currentTab == ScreenTab.Watchlist) Icons.Default.Bookmarks else Icons.Outlined.Bookmarks,
                            contentDescription = "Watchlist"
                        )
                    },
                    label = {
                        Text(
                            text = "Watchlist",
                            fontSize = 10.sp,
                            fontWeight = if (uiState.currentTab == ScreenTab.Watchlist) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AniCyanAccent,
                        indicatorColor = AniPurplePrimary,
                        unselectedIconColor = AniTextSecondary,
                        unselectedTextColor = AniTextSecondary
                    ),
                    modifier = Modifier.testTag("tab_watchlist")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    ScreenTab.Discover -> DiscoverScreen(
                        uiState = uiState,
                        onSearchChange = viewModel::onSearchQueryChanged,
                        onMoodSelect = viewModel::selectMoodFilter,
                        onGenreSelect = viewModel::selectGenreFilter,
                        onAnimeClick = viewModel::openAnimeDetail,
                        onBookmarkClick = { anime ->
                            viewModel.addToWatchlist(anime, WatchStatus.PLAN_TO_WATCH)
                        },
                        onStartQuiz = viewModel::startQuiz,
                        onNavigateSuggest = { viewModel.selectTab(ScreenTab.Suggest) },
                        onToggleLiveJikan = viewModel::toggleLiveJikanMode,
                        onSelectJikanTab = viewModel::setJikanSourceTab,
                        onRetryJikan = {
                            when (uiState.jikanSourceTab) {
                                "TOP_AIRING" -> viewModel.fetchTopAiringJikan()
                                "SEASONAL" -> viewModel.fetchSeasonalJikan()
                                else -> viewModel.executeLiveSearch(uiState.searchQuery)
                            }
                        }
                    )

                    ScreenTab.Suggest -> PreferenceSuggestionScreen(
                        filter = uiState.userPreferenceFilter,
                        suggestions = uiState.preferenceSuggestions,
                        onToggleMood = viewModel::togglePreferenceMood,
                        onToggleGenre = viewModel::togglePreferenceGenre,
                        onSetLength = viewModel::setPreferenceLength,
                        onSetMinScore = viewModel::setPreferenceMinScore,
                        onSetEra = viewModel::setPreferenceEra,
                        onResetPreferences = viewModel::resetPreferences,
                        onAnimeClick = viewModel::openAnimeDetail,
                        onAddToWatchlist = { anime ->
                            viewModel.addToWatchlist(anime, WatchStatus.PLAN_TO_WATCH)
                        }
                    )

                    ScreenTab.Surprise -> SurpriseScreen(
                        uiState = uiState,
                        onSpin = viewModel::spinSurpriseWheel,
                        onSetMoodFilter = viewModel::setSurpriseMoodFilter,
                        onSetFormatFilter = viewModel::setSurpriseFormatFilter,
                        onAnimeClick = viewModel::openAnimeDetail,
                        onAddToWatchlist = { anime ->
                            viewModel.addToWatchlist(anime, WatchStatus.PLAN_TO_WATCH)
                        }
                    )

                    ScreenTab.Matcher -> SimilarMatcherScreen(
                        uiState = uiState,
                        onSelectBaseAnime = viewModel::selectMatcherBaseAnime,
                        onAnimeClick = viewModel::openAnimeDetail
                    )

                    ScreenTab.Watchlist -> WatchlistScreen(
                        items = watchlistItems,
                        onAnimeClick = viewModel::openAnimeDetail,
                        onUpdateProgress = viewModel::updateWatchedEpisodes,
                        onRemove = viewModel::removeFromWatchlist,
                        onNavigateDiscover = { viewModel.selectTab(ScreenTab.Discover) }
                    )
                }
            }

            // Anime Detail Sheet
            uiState.selectedAnimeForDetail?.let { anime ->
                val currentWatchlistEntry = watchlistItems.find { it.animeId == anime.id }
                AnimeDetailSheet(
                    anime = anime,
                    sheetState = sheetState,
                    onDismiss = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            viewModel.closeAnimeDetail()
                        }
                    },
                    onSelectStatus = { status ->
                        viewModel.addToWatchlist(anime, status)
                    },
                    onToggleFavorite = {
                        viewModel.toggleFavorite(anime)
                    },
                    isFavorite = currentWatchlistEntry?.isFavorite ?: false,
                    currentStatus = currentWatchlistEntry?.status?.let {
                        try { WatchStatus.valueOf(it) } catch (e: Exception) { null }
                    },
                    onSelectSimilarAnime = { nextAnime ->
                        viewModel.openAnimeDetail(nextAnime)
                    }
                )
            }

            // 30-Sec Recommendation Quiz Dialog
            QuizDialog(
                quizState = uiState.quizState,
                onDismiss = viewModel::dismissQuiz,
                onSelectMood = viewModel::setQuizMood,
                onSelectFormat = viewModel::setQuizFormat,
                onSelectPacing = viewModel::finishQuiz,
                onAnimeClick = viewModel::openAnimeDetail
            )
        }
    }
}
