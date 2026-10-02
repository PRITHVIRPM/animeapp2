package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Anime
import com.example.model.AnimeMood
import com.example.ui.components.AnimePosterCard
import com.example.ui.components.HeroBanner
import com.example.ui.components.MoodChip
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.viewmodel.UiState

@Composable
fun DiscoverScreen(
    uiState: UiState,
    onSearchChange: (String) -> Unit,
    onMoodSelect: (AnimeMood?) -> Unit,
    onGenreSelect: (String?) -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onBookmarkClick: (Anime) -> Unit,
    onStartQuiz: () -> Unit,
    onNavigateSuggest: () -> Unit = {},
    onToggleLiveJikan: (Boolean) -> Unit = {},
    onSelectJikanTab: (String) -> Unit = {},
    onRetryJikan: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val genres = listOf("All", "Action", "Adventure", "Drama", "Comedy", "Sci-Fi", "Fantasy", "Mystery", "Slice of Life", "Romance")

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier
            .fillMaxSize()
            .testTag("discover_screen"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Banner
        item(span = { GridItemSpan(2) }) {
            HeroBanner(
                onQuizClick = onStartQuiz,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // Custom Preference Matcher Callout
        item(span = { GridItemSpan(2) }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onNavigateSuggest)
                    .testTag("matchmaker_callout_card"),
                shape = RoundedCornerShape(14.dp),
                color = AniPurplePrimary.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "🎯", fontSize = 24.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Preference Suggestion Matcher",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Filter by mood, multiple genres, duration & era for tailored matches",
                            fontSize = 11.sp,
                            color = AniTextSecondary
                        )
                    }
                    Text(
                        text = "Tune →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniCyanAccent
                    )
                }
            }
        }

        // API Switcher: Curated vs Live Jikan MAL API
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleLiveJikan(false) }
                        .testTag("catalog_curated_tab"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (!uiState.isLiveJikanMode) AniPurplePrimary else AniDarkSurface,
                    border = BorderStroke(1.dp, if (!uiState.isLiveJikanMode) AniPurplePrimary else AniCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "⚡", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Curated Hits",
                            fontSize = 12.sp,
                            fontWeight = if (!uiState.isLiveJikanMode) FontWeight.Bold else FontWeight.Normal,
                            color = if (!uiState.isLiveJikanMode) Color.White else AniTextSecondary
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleLiveJikan(true) }
                        .testTag("catalog_jikan_tab"),
                    shape = RoundedCornerShape(12.dp),
                    color = if (uiState.isLiveJikanMode) AniCyanAccent else AniDarkSurface,
                    border = BorderStroke(1.dp, if (uiState.isLiveJikanMode) AniCyanAccent else AniCardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🌐", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Jikan API",
                            fontSize = 12.sp,
                            fontWeight = if (uiState.isLiveJikanMode) FontWeight.Bold else FontWeight.Normal,
                            color = if (uiState.isLiveJikanMode) Color.Black else AniTextSecondary
                        )
                    }
                }
            }
        }

        // Live Jikan Sub-tabs
        if (uiState.isLiveJikanMode) {
            item(span = { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tabs = listOf(
                        "SEARCH" to "🔍 Search 50k+",
                        "TOP_AIRING" to "🔥 Top Airing",
                        "SEASONAL" to "❄️ This Season"
                    )

                    tabs.forEach { (key, label) ->
                        val isSelected = uiState.jikanSourceTab == key
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSelectJikanTab(key) }
                                .testTag("jikan_subtab_$key"),
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) AniCyanAccent.copy(alpha = 0.2f) else AniDarkSurfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) AniCyanAccent else AniCardBorder)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AniCyanAccent else AniTextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Search Bar
        item(span = { GridItemSpan(2) }) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = if (uiState.isLiveJikanMode) "Search any anime online via Jikan API..." else "Search anime, studio, genre...",
                        color = AniTextSecondary,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (uiState.isLiveJikanMode) AniCyanAccent else AniPurplePrimary
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = AniTextSecondary
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_text_field"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (uiState.isLiveJikanMode) AniCyanAccent else AniPurplePrimary,
                    unfocusedBorderColor = AniCardBorder,
                    focusedContainerColor = AniDarkSurface,
                    unfocusedContainerColor = AniDarkSurface,
                    focusedTextColor = AniTextPrimary,
                    unfocusedTextColor = AniTextPrimary
                ),
                singleLine = true
            )
        }

        // Curated Mood & Genre Filters (When in Curated mode)
        if (!uiState.isLiveJikanMode) {
            // "Find by Mood" Carousel
            item(span = { GridItemSpan(2) }) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Find By Mood",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AniTextPrimary
                        )
                        if (uiState.selectedMood != null) {
                            Text(
                                text = "Clear filter",
                                fontSize = 12.sp,
                                color = AniCyanAccent,
                                modifier = Modifier.clickable { onMoodSelect(null) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(AnimeMood.values()) { mood ->
                            MoodChip(
                                mood = mood,
                                isSelected = uiState.selectedMood == mood,
                                onClick = { onMoodSelect(mood) }
                            )
                        }
                    }
                }
            }

            // Genre Filter Chips
            item(span = { GridItemSpan(2) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(genres) { genre ->
                        val isAll = genre == "All"
                        val isSelected = if (isAll) uiState.selectedGenre == null else uiState.selectedGenre == genre

                        Surface(
                            modifier = Modifier
                                .testTag("genre_chip_$genre")
                                .clickable {
                                    onGenreSelect(if (isAll) null else genre)
                                },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) AniCyanAccent else AniDarkSurface,
                            border = BorderStroke(1.dp, if (isSelected) AniCyanAccent else AniCardBorder)
                        ) {
                            Text(
                                text = genre,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.Black else AniTextSecondary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Jikan Loading / Error Indicator
        if (uiState.isLiveJikanMode) {
            if (uiState.isJikanLoading) {
                item(span = { GridItemSpan(2) }) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = AniDarkSurface,
                        border = BorderStroke(1.dp, AniCardBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AniCyanAccent,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Searching live anime database...",
                                fontSize = 13.sp,
                                color = AniCyanAccent
                            )
                        }
                    }
                }
            }

            if (uiState.jikanErrorMessage != null && !uiState.isJikanLoading) {
                item(span = { GridItemSpan(2) }) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0x33EF4444),
                        border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = uiState.jikanErrorMessage ?: "Jikan API request failed",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = onRetryJikan, modifier = Modifier.size(24.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Retry",
                                    tint = AniCyanAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Recommendations / Results Count
        item(span = { GridItemSpan(2) }) {
            val count = if (uiState.isLiveJikanMode) uiState.liveJikanResults.size else uiState.filteredAnimeList.size
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        uiState.isLiveJikanMode -> when (uiState.jikanSourceTab) {
                            "TOP_AIRING" -> "Live Top Airing Anime (Jikan)"
                            "SEASONAL" -> "Live Current Season (Jikan)"
                            else -> if (uiState.searchQuery.isNotBlank()) "Jikan Search Results" else "Online Anime (Jikan)"
                        }
                        uiState.selectedMood != null -> "${uiState.selectedMood.title} Picks"
                        uiState.selectedGenre != null -> "${uiState.selectedGenre} Anime"
                        uiState.searchQuery.isNotBlank() -> "Search Results"
                        else -> "Top Suggestions For You"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniTextPrimary
                )

                Text(
                    text = "$count shows",
                    fontSize = 12.sp,
                    color = AniTextSecondary
                )
            }
        }

        // Anime Posters Grid
        val displayList = if (uiState.isLiveJikanMode) uiState.liveJikanResults else uiState.filteredAnimeList
        items(displayList, key = { it.id }) { anime ->
            AnimePosterCard(
                anime = anime,
                onClick = { onAnimeClick(anime) },
                onBookmarkClick = { onBookmarkClick(anime) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
