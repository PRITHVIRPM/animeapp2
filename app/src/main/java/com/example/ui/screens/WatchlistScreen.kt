package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.AnimeCatalog
import com.example.data.local.WatchStatus
import com.example.data.local.WatchlistEntity
import com.example.model.Anime
import com.example.ui.components.ScoreBadge
import com.example.ui.components.WatchlistPosterImage
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniGoldRating
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary

@Composable
fun WatchlistScreen(
    items: List<WatchlistEntity>,
    onAnimeClick: (Anime) -> Unit,
    onUpdateProgress: (Int, Int) -> Unit,
    onRemove: (Int) -> Unit,
    onNavigateDiscover: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filterOptions = listOf(
        "ALL" to "All (${items.size})",
        "PLAN_TO_WATCH" to "Plan to Watch",
        "WATCHING" to "Watching",
        "COMPLETED" to "Completed",
        "FAVORITES" to "Favorites"
    )

    val filteredList = items.filter { item ->
        when (selectedFilter) {
            "ALL" -> true
            "FAVORITES" -> item.isFavorite
            else -> item.status == selectedFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("watchlist_screen")
    ) {
        // Header
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Text(
                text = "My Anime Watchlist 📌",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AniTextPrimary
            )
            Text(
                text = "Keep track of your journey, episode progress and favorites",
                fontSize = 13.sp,
                color = AniTextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filterOptions) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Surface(
                        modifier = Modifier
                            .testTag("filter_tab_$key")
                            .clickable { selectedFilter = key },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) AniPurplePrimary else AniDarkSurface,
                        border = BorderStroke(1.dp, if (isSelected) AniPurplePrimary else AniCardBorder)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else AniTextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredList.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = AniDarkSurfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "📺", fontSize = 36.sp)
                        }
                    }
                    Text(
                        text = "Your Watchlist is Empty",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniTextPrimary
                    )
                    Text(
                        text = "Explore anime suggestions and tap the bookmark icon to start tracking your next binge!",
                        fontSize = 13.sp,
                        color = AniTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = onNavigateDiscover,
                        colors = ButtonDefaults.buttonColors(containerColor = AniPurplePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Discover Suggestions", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList, key = { it.animeId }) { entry ->
                    WatchlistCard(
                        entry = entry,
                        onClick = {
                            val anime = AnimeCatalog.getAnimeById(entry.animeId)
                            if (anime != null) onAnimeClick(anime)
                        },
                        onIncrementProgress = {
                            val next = (entry.watchedEpisodes + 1).coerceAtMost(entry.totalEpisodes)
                            onUpdateProgress(entry.animeId, next)
                        },
                        onRemove = { onRemove(entry.animeId) }
                    )
                }
            }
        }
    }
}

@Composable
fun WatchlistCard(
    entry: WatchlistEntity,
    onClick: () -> Unit,
    onIncrementProgress: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("watchlist_item_${entry.animeId}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
        border = BorderStroke(1.dp, AniCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            val liveCoverUrl = com.example.data.AnimeCatalog.getAnimeById(entry.animeId)?.coverUrl ?: entry.coverUrl
            Card(
                modifier = Modifier
                    .width(72.dp)
                    .height(98.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                WatchlistPosterImage(
                    title = entry.title,
                    coverUrl = liveCoverUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Info & Progress
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniTextPrimary,
                        maxLines = 1,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Remove",
                            tint = AniTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = AniDarkSurfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = when (entry.status) {
                                "PLAN_TO_WATCH" -> "Plan to Watch"
                                "WATCHING" -> "Watching"
                                "COMPLETED" -> "Completed"
                                else -> "Favorite"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AniCyanAccent,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (entry.isFavorite) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = Color(0xFFF43F5E),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    ScoreBadge(score = entry.score)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Episode Progress Indicator & +1 Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Progress",
                                fontSize = 10.sp,
                                color = AniTextSecondary
                            )
                            Text(
                                text = "${entry.watchedEpisodes}/${entry.totalEpisodes} eps",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        val progressFraction = if (entry.totalEpisodes > 0) {
                            entry.watchedEpisodes.toFloat() / entry.totalEpisodes.toFloat()
                        } else 0f
                        LinearProgressIndicator(
                            progress = { progressFraction.coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp),
                            color = AniCyanAccent,
                            trackColor = AniDarkSurfaceVariant
                        )
                    }

                    if (entry.watchedEpisodes < entry.totalEpisodes) {
                        IconButton(
                            onClick = onIncrementProgress,
                            modifier = Modifier
                                .size(32.dp)
                                .background(AniPurplePrimary, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add episode watched",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
