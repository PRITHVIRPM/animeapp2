package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.Anime
import com.example.model.AnimeMood
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniGoldRating
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary

@Composable
fun AnimePosterImage(
    anime: Anime,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageRequest = androidx.compose.runtime.remember(anime.coverUrl) {
        ImageRequest.Builder(context)
            .data(anime.coverUrl)
            .crossfade(true)
            .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10)")
            .build()
    }
    val themeColor = Color(anime.vibes.firstOrNull()?.colorHex ?: 0xFFA855F7)

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = anime.title,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.35f),
                                AniDarkSurfaceVariant
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = anime.title.take(1),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AniCyanAccent.copy(alpha = 0.6f)
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                themeColor.copy(alpha = 0.5f),
                                AniDarkSurface
                            )
                        )
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = anime.vibes.firstOrNull()?.icon ?: "🎬",
                        fontSize = 22.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = anime.title,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}

@Composable
fun WatchlistPosterImage(
    title: String,
    coverUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageRequest = androidx.compose.runtime.remember(coverUrl) {
        ImageRequest.Builder(context)
            .data(coverUrl)
            .crossfade(true)
            .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10)")
            .build()
    }

    SubcomposeAsyncImage(
        model = imageRequest,
        contentDescription = title,
        modifier = modifier,
        contentScale = contentScale,
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AniDarkSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title.take(1),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniCyanAccent.copy(alpha = 0.5f)
                )
            }
        },
        error = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AniDarkSurface)
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}

@Composable
fun ScoreBadge(
    score: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xCC120E24),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, AniGoldRating.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = AniGoldRating,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = String.format("%.2f", score),
                color = AniGoldRating,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AnimePosterCard(
    anime: Anime,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    isBookmarked: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("anime_card_${anime.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
        border = BorderStroke(1.dp, AniCardBorder)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                AnimePosterImage(
                    anime = anime,
                    modifier = Modifier.fillMaxSize()
                )

                // Top gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                )

                // Bottom gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, AniDarkSurface)
                            )
                        )
                )

                ScoreBadge(
                    score = anime.score,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(36.dp)
                        .background(Color(0x99120E24), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) AniCyanAccent else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Format & Year tag
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = AniDarkSurfaceVariant.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${anime.format.name} • ${anime.year}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = AniCyanAccent,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = anime.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = anime.genres.take(2).joinToString(" • "),
                    fontSize = 11.sp,
                    color = AniTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Studio & episodes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = anime.studio,
                        fontSize = 10.sp,
                        color = AniPurplePrimary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${anime.episodes} eps",
                        fontSize = 10.sp,
                        color = AniTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun HeroBanner(
    onQuizClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = AniDarkSurface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_anime_hero),
                contentDescription = "Anime Hero Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Multi-gradient overlay for contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0x33120E24),
                                Color(0xCC0D0B18),
                                Color(0xF00D0B18)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Surface(
                    color = AniPurplePrimary.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.6f))
                ) {
                    Text(
                        text = "✨ AI-POWERED ANIME COMPASS",
                        color = AniCyanAccent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Find Your Next Obsession",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Answer 3 quick questions for tailored anime suggestions",
                    color = AniTextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onQuizClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AniPurplePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("start_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Explore,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Take 30-Sec Quiz",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MoodChip(
    mood: AnimeMood,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .testTag("mood_chip_${mood.name}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = if (isSelected) Color(mood.colorHex).copy(alpha = 0.25f) else AniDarkSurface,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Color(mood.colorHex) else AniCardBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = mood.icon, fontSize = 16.sp)
            Text(
                text = mood.title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else AniTextSecondary
            )
        }
    }
}
