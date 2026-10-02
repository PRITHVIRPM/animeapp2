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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.WatchStatus
import com.example.ui.components.AnimeDetailSheet
import com.example.ui.components.DesktopWebView
import com.example.ui.components.QuizDialog
import com.example.ui.screens.DiscoverScreen
import com.example.ui.screens.PreferenceSuggestionScreen
import com.example.ui.screens.SimilarMatcherScreen
import com.example.ui.screens.SurpriseScreen
import com.example.ui.screens.WatchlistScreen
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkBg
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AnimeViewModel
import com.example.ui.viewmodel.ScreenTab

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

    // Toggle between Native Android App and Desktop Web View
    var isWebViewMode by rememberSaveable { mutableStateOf(false) }

    // Handle system back navigation
    BackHandler(
        enabled = isWebViewMode ||
                uiState.selectedAnimeForDetail != null ||
                uiState.quizState.isOpen ||
                uiState.currentTab != ScreenTab.Discover
    ) {
        when {
            isWebViewMode -> isWebViewMode = false
            uiState.selectedAnimeForDetail != null -> viewModel.closeAnimeDetail()
            uiState.quizState.isOpen -> viewModel.dismissQuiz()
            uiState.currentTab != ScreenTab.Discover -> viewModel.selectTab(ScreenTab.Discover)
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 720.dp

        Scaffold(
            containerColor = AniDarkBg,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AniSuggest",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = AniTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AniCyanAccent.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, AniCyanAccent.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (isWebViewMode) "DESKTOP WEB" else if (isWideScreen) "DESKTOP APP" else "HYBRID ENGINE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AniCyanAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        FilledTonalButton(
                            onClick = { isWebViewMode = !isWebViewMode },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = if (isWebViewMode) AniPurplePrimary else AniDarkSurfaceVariant,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("toggle_web_view_mode")
                        ) {
                            Icon(
                                imageVector = if (isWebViewMode) Icons.Default.PhoneAndroid else Icons.Default.Language,
                                contentDescription = "Switch View",
                                modifier = Modifier.size(16.dp),
                                tint = if (isWebViewMode) Color.White else AniCyanAccent
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isWebViewMode) "App Mode" else "Web Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = AniDarkSurface,
                        titleContentColor = AniTextPrimary
                    )
                )
            },
            bottomBar = {
                if (!isWebViewMode && !isWideScreen) {
                    NavigationBar(
                        containerColor = AniDarkSurface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        // Discover
                        NavigationBarItem(
                            selected = uiState.currentTab == ScreenTab.Discover,
                            onClick = { viewModel.selectTab(ScreenTab.Discover) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == ScreenTab.Discover) Icons.Default.Explore else Icons.Outlined.Explore,
                                    contentDescription = "Discover"
                                )
                            },
                            label = { Text("Discover", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = AniCyanAccent,
                                indicatorColor = AniPurplePrimary,
                                unselectedIconColor = AniTextSecondary,
                                unselectedTextColor = AniTextSecondary
                            ),
                            modifier = Modifier.testTag("tab_discover")
                        )

                        // Suggest
                        NavigationBarItem(
                            selected = uiState.currentTab == ScreenTab.Suggest,
                            onClick = { viewModel.selectTab(ScreenTab.Suggest) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == ScreenTab.Suggest) Icons.Default.Tune else Icons.Outlined.Tune,
                                    contentDescription = "Suggest"
                                )
                            },
                            label = { Text("Suggest", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = AniCyanAccent,
                                indicatorColor = AniPurplePrimary,
                                unselectedIconColor = AniTextSecondary,
                                unselectedTextColor = AniTextSecondary
                            ),
                            modifier = Modifier.testTag("tab_suggest")
                        )

                        // Roulette
                        NavigationBarItem(
                            selected = uiState.currentTab == ScreenTab.Surprise,
                            onClick = { viewModel.selectTab(ScreenTab.Surprise) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == ScreenTab.Surprise) Icons.Default.Casino else Icons.Outlined.Casino,
                                    contentDescription = "Roulette"
                                )
                            },
                            label = { Text("Roulette", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = AniCyanAccent,
                                indicatorColor = AniPurplePrimary,
                                unselectedIconColor = AniTextSecondary,
                                unselectedTextColor = AniTextSecondary
                            ),
                            modifier = Modifier.testTag("tab_surprise")
                        )

                        // Similar
                        NavigationBarItem(
                            selected = uiState.currentTab == ScreenTab.Matcher,
                            onClick = { viewModel.selectTab(ScreenTab.Matcher) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == ScreenTab.Matcher) Icons.Default.AutoAwesome else Icons.Outlined.AutoAwesome,
                                    contentDescription = "Similar"
                                )
                            },
                            label = { Text("Similar", fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = AniCyanAccent,
                                indicatorColor = AniPurplePrimary,
                                unselectedIconColor = AniTextSecondary,
                                unselectedTextColor = AniTextSecondary
                            ),
                            modifier = Modifier.testTag("tab_matcher")
                        )

                        // Watchlist
                        NavigationBarItem(
                            selected = uiState.currentTab == ScreenTab.Watchlist,
                            onClick = { viewModel.selectTab(ScreenTab.Watchlist) },
                            icon = {
                                Icon(
                                    imageVector = if (uiState.currentTab == ScreenTab.Watchlist) Icons.Default.Bookmarks else Icons.Outlined.Bookmarks,
                                    contentDescription = "Watchlist"
                                )
                            },
                            label = { Text("Watchlist", fontSize = 10.sp) },
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
            }
        ) { innerPadding ->
            if (isWebViewMode) {
                // Interactive Desktop Web View embedded directly
                DesktopWebView(modifier = Modifier.padding(innerPadding))
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    // Desktop / Tablet Navigation Rail
                    if (isWideScreen) {
                        NavigationRail(
                            containerColor = AniDarkSurface,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            NavigationRailItem(
                                selected = uiState.currentTab == ScreenTab.Discover,
                                onClick = { viewModel.selectTab(ScreenTab.Discover) },
                                icon = { Icon(Icons.Default.Explore, contentDescription = "Discover") },
                                label = { Text("Discover") },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    indicatorColor = AniPurplePrimary
                                )
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == ScreenTab.Suggest,
                                onClick = { viewModel.selectTab(ScreenTab.Suggest) },
                                icon = { Icon(Icons.Default.Tune, contentDescription = "Suggest") },
                                label = { Text("Suggest") },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    indicatorColor = AniPurplePrimary
                                )
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == ScreenTab.Surprise,
                                onClick = { viewModel.selectTab(ScreenTab.Surprise) },
                                icon = { Icon(Icons.Default.Casino, contentDescription = "Roulette") },
                                label = { Text("Roulette") },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    indicatorColor = AniPurplePrimary
                                )
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == ScreenTab.Matcher,
                                onClick = { viewModel.selectTab(ScreenTab.Matcher) },
                                icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Similar") },
                                label = { Text("Similar") },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    indicatorColor = AniPurplePrimary
                                )
                            )
                            NavigationRailItem(
                                selected = uiState.currentTab == ScreenTab.Watchlist,
                                onClick = { viewModel.selectTab(ScreenTab.Watchlist) },
                                icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Watchlist") },
                                label = { Text("Watchlist") },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    indicatorColor = AniPurplePrimary
                                )
                            )
                        }
                    }

                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
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
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Anime Detail View
    if (uiState.selectedAnimeForDetail != null) {
        val selected = uiState.selectedAnimeForDetail!!
        val currentEntity = watchlistItems.find { it.animeId == selected.id }
        AnimeDetailSheet(
            anime = selected,
            sheetState = sheetState,
            onDismiss = viewModel::closeAnimeDetail,
            onSelectStatus = { status -> viewModel.addToWatchlist(selected, status) },
            onToggleFavorite = { viewModel.toggleFavorite(selected) },
            isFavorite = currentEntity?.isFavorite == true,
            currentStatus = currentEntity?.status?.let { s -> runCatching { WatchStatus.valueOf(s) }.getOrNull() },
            onSelectSimilarAnime = { similar -> viewModel.openAnimeDetail(similar) }
        )
    }

    // 30-Second Quiz Dialog
    if (uiState.quizState.isOpen) {
        QuizDialog(
            quizState = uiState.quizState,
            onDismiss = viewModel::dismissQuiz,
            onSelectMood = viewModel::setQuizMood,
            onSelectFormat = viewModel::setQuizFormat,
            onSelectPacing = viewModel::finishQuiz,
            onAnimeClick = { anime ->
                viewModel.dismissQuiz()
                viewModel.openAnimeDetail(anime)
            }
        )
    }
}
