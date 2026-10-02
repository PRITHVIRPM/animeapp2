package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ScoredAnimeSuggestion
import com.example.data.UserPreferenceFilter
import com.example.model.Anime
import com.example.model.AnimeMood
import com.example.ui.components.AnimePosterImage
import com.example.ui.components.ScoreBadge
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniGoldRating
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferenceSuggestionScreen(
    filter: UserPreferenceFilter,
    suggestions: List<ScoredAnimeSuggestion>,
    onToggleMood: (AnimeMood) -> Unit,
    onToggleGenre: (String) -> Unit,
    onSetLength: (String) -> Unit,
    onSetMinScore: (Double) -> Unit,
    onSetEra: (String) -> Unit,
    onResetPreferences: () -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onAddToWatchlist: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {
    val genreOptions = listOf(
        "Action", "Adventure", "Drama", "Comedy", "Sci-Fi",
        "Fantasy", "Mystery", "Slice of Life", "Romance", "Suspense"
    )

    val lengthOptions = listOf(
        "ANY" to "Any Length",
        "QUICK" to "⚡ Quick Binge (10-13 eps)",
        "STANDARD" to "📺 Standard (24-28 eps)",
        "EPIC" to "🏰 Epic Journey (36+ eps)",
        "MOVIE" to "🎬 Movie (~2 hrs)"
    )

    val scoreOptions = listOf(
        0.0 to "Any Rating",
        8.0 to "⭐ 8.0+",
        8.5 to "🌟 8.5+ Great",
        9.0 to "👑 9.0+ Masterpiece"
    )

    val eraOptions = listOf(
        "ALL" to "All Time",
        "MODERN" to "✨ Modern (2020+)",
        "RECENT" to "🔥 Recent (2015-2019)",
        "CLASSIC" to "📼 Classic (≤2014)"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("preference_suggestion_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Anime Matchmaker 🎯",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AniTextPrimary
                        )
                        Text(
                            text = "Tune your exact preferences to discover your customized anime match",
                            fontSize = 13.sp,
                            color = AniTextSecondary
                        )
                    }
                    OutlinedButton(
                        onClick = onResetPreferences,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, AniCardBorder),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("reset_preferences_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
                            tint = AniCyanAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Reset", fontSize = 11.sp, color = AniCyanAccent)
                    }
                }
            }
        }

        // Section 1: Mood & Vibe Preference
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "1. Desired Mood / Vibe", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AniTextPrimary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "(Multi-select)", fontSize = 11.sp, color = AniTextSecondary)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AnimeMood.values().forEach { mood ->
                            val isSelected = filter.selectedMoods.contains(mood)
                            Surface(
                                modifier = Modifier
                                    .testTag("pref_mood_${mood.name}")
                                    .clickable { onToggleMood(mood) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(mood.colorHex).copy(alpha = 0.3f) else AniDarkSurfaceVariant,
                                border = BorderStroke(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) Color(mood.colorHex) else AniCardBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = mood.icon, fontSize = 14.sp)
                                    Text(
                                        text = mood.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else AniTextSecondary
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(mood.colorHex),
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 2: Genres
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "2. Preferred Genres", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AniTextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        genreOptions.forEach { genre ->
                            val isSelected = filter.selectedGenres.contains(genre)
                            Surface(
                                modifier = Modifier
                                    .testTag("pref_genre_$genre")
                                    .clickable { onToggleGenre(genre) },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) AniCyanAccent else AniDarkSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) AniCyanAccent else AniCardBorder)
                            ) {
                                Text(
                                    text = genre,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else AniTextSecondary,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Length & Minimum Score
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "3. Anime Length & Format", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AniTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        lengthOptions.forEach { (key, label) ->
                            val isSelected = filter.lengthCategory == key
                            Surface(
                                modifier = Modifier
                                    .testTag("pref_length_$key")
                                    .clickable { onSetLength(key) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) AniPurplePrimary else AniDarkSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) AniPurplePrimary else AniCardBorder)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else AniTextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "4. Minimum Score Threshold", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AniTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        scoreOptions.forEach { (score, label) ->
                            val isSelected = filter.minScore == score
                            Surface(
                                modifier = Modifier
                                    .testTag("pref_score_$score")
                                    .clickable { onSetMinScore(score) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) AniGoldRating else AniDarkSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) AniGoldRating else AniCardBorder)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else AniTextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "5. Release Era", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AniTextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        eraOptions.forEach { (era, label) ->
                            val isSelected = filter.releaseEra == era
                            Surface(
                                modifier = Modifier
                                    .testTag("pref_era_$era")
                                    .clickable { onSetEra(era) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) AniPurplePrimary.copy(alpha = 0.5f) else AniDarkSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) AniPurplePrimary else AniCardBorder)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else AniTextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section Title: Match Results
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personalized Recommendations (${suggestions.size})",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AniTextPrimary
                )
            }
        }

        // Top #1 Spotlight Match
        if (suggestions.isNotEmpty()) {
            val topMatch = suggestions.first()
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("top_match_card")
                        .clickable { onAnimeClick(topMatch.anime) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                    border = BorderStroke(2.dp, AniCyanAccent)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                        ) {
                            AnimePosterImage(
                                anime = topMatch.anime,
                                modifier = Modifier.fillMaxSize()
                            )

                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp),
                                color = AniCyanAccent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "👑 TOP MATCH: ${topMatch.matchPercentage}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            ScoreBadge(
                                score = topMatch.anime.score,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = topMatch.anime.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary
                            )
                            Text(
                                text = "${topMatch.anime.studio} • ${topMatch.anime.episodes} episodes • ${topMatch.anime.year}",
                                fontSize = 12.sp,
                                color = AniCyanAccent
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "“${topMatch.anime.hook}”",
                                fontSize = 12.sp,
                                color = AniTextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Why this matches your preferences:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            topMatch.matchReasons.forEach { reason ->
                                Row(
                                    modifier = Modifier.padding(vertical = 1.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "✓ ", color = AniCyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(text = reason, fontSize = 12.sp, color = AniTextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onAddToWatchlist(topMatch.anime) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, AniPurplePrimary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdd,
                                        contentDescription = null,
                                        tint = AniPurplePrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Watchlist", color = Color.White, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { onAnimeClick(topMatch.anime) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AniCyanAccent)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "View Details", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Runner Up Matches
        items(suggestions.drop(1)) { suggestion ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnimeClick(suggestion.anime) }
                    .testTag("suggestion_card_${suggestion.anime.id}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .width(76.dp)
                            .height(105.dp),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        AnimePosterImage(
                            anime = suggestion.anime,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = suggestion.anime.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = AniPurplePrimary.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(6.dp),
                                border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = "${suggestion.matchPercentage}%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AniCyanAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "${suggestion.anime.genres.take(2).joinToString(" • ")} • ${suggestion.anime.episodes} eps",
                            fontSize = 11.sp,
                            color = AniTextSecondary
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = suggestion.matchReasons.firstOrNull() ?: suggestion.anime.hook,
                            fontSize = 11.sp,
                            color = AniCyanAccent,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
