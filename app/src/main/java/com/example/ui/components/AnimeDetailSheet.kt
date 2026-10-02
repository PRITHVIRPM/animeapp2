package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.graphics.Brush
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
import com.example.model.Anime
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkBg
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniGoldRating
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AnimeDetailSheet(
    anime: Anime,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSelectStatus: (WatchStatus) -> Unit,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean,
    currentStatus: WatchStatus?,
    onSelectSimilarAnime: (Anime) -> Unit
) {
    var expandedSynopsis by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = AniDarkBg,
        tonalElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header Image & overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AnimePosterImage(
                    anime = anime,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.4f),
                                    Color(0xCC0D0B18),
                                    AniDarkBg
                                )
                            )
                        )
                )

                // Close button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color(0x99120E24), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                // Favorite button
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(Color(0x99120E24), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFF43F5E) else Color.White
                    )
                }

                // Poster & Title Info inside Header Bottom
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .width(100.dp)
                            .height(145.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, AniPurplePrimary.copy(alpha = 0.7f)),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        AnimePosterImage(
                            anime = anime,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = anime.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AniTextPrimary,
                            lineHeight = 24.sp
                        )
                        Text(
                            text = anime.japaneseTitle,
                            fontSize = 12.sp,
                            color = AniTextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ScoreBadge(score = anime.score)

                            Surface(
                                color = AniDarkSurfaceVariant,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${anime.episodes} Eps",
                                    fontSize = 11.sp,
                                    color = AniCyanAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Killer Hook Highlight Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                color = AniPurplePrimary.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🔥", fontSize = 22.sp)
                    Column {
                        Text(
                            text = "THE HOOK",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AniCyanAccent,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = anime.hook,
                            fontSize = 13.sp,
                            color = AniTextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metadata Row: Studio, Year, Duration, Format
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetaInfoItem(label = "Studio", value = anime.studio)
                MetaInfoItem(label = "Release", value = "${anime.year}")
                MetaInfoItem(label = "Length", value = anime.duration)
                MetaInfoItem(label = "Format", value = anime.format.displayName)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Watchlist Action Bar
            Text(
                text = "Watchlist Status",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = AniTextPrimary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WatchStatus.values().forEach { status ->
                    val isSelected = currentStatus == status
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSelectStatus(status) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) AniPurplePrimary else AniDarkSurface,
                        border = BorderStroke(1.dp, if (isSelected) AniPurplePrimary else AniCardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (status) {
                                    WatchStatus.PLAN_TO_WATCH -> "Plan"
                                    WatchStatus.WATCHING -> "Watching"
                                    WatchStatus.COMPLETED -> "Done"
                                    WatchStatus.FAVORITE -> "Fav"
                                },
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else AniTextSecondary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Synopsis
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Synopsis",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniTextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = anime.synopsis,
                    fontSize = 13.sp,
                    color = AniTextSecondary,
                    lineHeight = 20.sp,
                    maxLines = if (expandedSynopsis) Int.MAX_VALUE else 3
                )
                Text(
                    text = if (expandedSynopsis) "Show Less" else "Read More",
                    fontSize = 12.sp,
                    color = AniCyanAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { expandedSynopsis = !expandedSynopsis }
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Genres & Tags
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Genres & Themes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniTextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    anime.genres.forEach { genre ->
                        Surface(
                            color = AniDarkSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AniCardBorder)
                        ) {
                            Text(
                                text = genre,
                                fontSize = 11.sp,
                                color = AniTextPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                    anime.tags.forEach { tag ->
                        Surface(
                            color = Color(0x2222D3EE),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, AniCyanAccent.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "#$tag",
                                fontSize = 11.sp,
                                color = AniCyanAccent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Similar Recommendations
            val similarAnimeList = AnimeCatalog.getSimilarAnime(anime)
            if (similarAnimeList.isNotEmpty()) {
                Column {
                    Text(
                        text = "Because you viewed ${anime.title}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniTextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                    ) {
                        items(similarAnimeList) { similar ->
                            AnimePosterCard(
                                anime = similar,
                                onClick = { onSelectSimilarAnime(similar) },
                                onBookmarkClick = { onSelectStatus(WatchStatus.PLAN_TO_WATCH) },
                                modifier = Modifier.width(140.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetaInfoItem(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, AniCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = AniTextSecondary)
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = AniTextPrimary,
                maxLines = 1
            )
        }
    }
}
