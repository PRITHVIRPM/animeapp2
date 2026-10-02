package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Casino
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
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
import com.example.model.Anime
import com.example.model.AnimeFormat
import com.example.model.AnimeMood
import com.example.ui.components.AnimePosterImage
import com.example.ui.components.ScoreBadge
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkBg
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniGoldRating
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SurpriseScreen(
    uiState: UiState,
    onSpin: () -> Unit,
    onSetMoodFilter: (AnimeMood?) -> Unit,
    onSetFormatFilter: (AnimeFormat?) -> Unit,
    onAnimeClick: (Anime) -> Unit,
    onAddToWatchlist: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(uiState.isSpinning) {
        if (uiState.isSpinning) {
            rotation.animateTo(
                targetValue = rotation.value + 360f * 2,
                animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("surprise_screen"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title Header
        Text(
            text = "Anime Roulette 🎲",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AniTextPrimary
        )
        Text(
            text = "Can't decide? Let fate pick your next anime obsession!",
            fontSize = 13.sp,
            color = AniTextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Optional mood filters
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Target Mood (Optional)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AniTextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AnimeMood.values().take(6).forEach { mood ->
                    val isSelected = uiState.surpriseMoodFilter == mood
                    Surface(
                        modifier = Modifier.clickable { onSetMoodFilter(mood) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(mood.colorHex).copy(alpha = 0.3f) else AniDarkSurface,
                        border = BorderStroke(1.dp, if (isSelected) Color(mood.colorHex) else AniCardBorder)
                    ) {
                        Text(
                            text = "${mood.icon} ${mood.title}",
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else AniTextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big Spin Action Button
        Button(
            onClick = onSpin,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("spin_wheel_button"),
            colors = ButtonDefaults.buttonColors(containerColor = AniPurplePrimary),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Casino,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotation.value),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = if (uiState.isSpinning) "Selecting Destiny..." else "Spin For A Recommendation!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result Card
        val pickedAnime = uiState.surpriseAnime
        if (pickedAnime != null) {
            AnimatedVisibility(
                visible = !uiState.isSpinning,
                enter = fadeIn() + scaleIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("surprise_result_card")
                        .clickable { onAnimeClick(pickedAnime) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                    border = BorderStroke(1.5.dp, AniCyanAccent.copy(alpha = 0.6f))
                ) {
                    Column {
                        // Poster Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            AnimePosterImage(
                                anime = pickedAnime,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color.Transparent,
                                                Color.Black.copy(alpha = 0.6f),
                                                AniDarkSurface
                                            )
                                        )
                                    )
                            )

                            ScoreBadge(
                                score = pickedAnime.score,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(12.dp)
                            )

                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp),
                                color = AniCyanAccent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "🔥 98% MATCH",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = pickedAnime.title,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${pickedAnime.studio} • ${pickedAnime.episodes} Episodes • ${pickedAnime.year}",
                                    fontSize = 12.sp,
                                    color = AniCyanAccent
                                )
                            }
                        }

                        // Pitch & Reasons
                        Column(modifier = Modifier.padding(16.dp)) {
                            Surface(
                                color = AniPurplePrimary.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "“${pickedAnime.hook}”",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AniTextPrimary,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Why You'll Love It:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            pickedAnime.matchReasons.forEach { reason ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "✦ ", color = AniCyanAccent, fontSize = 12.sp)
                                    Text(text = reason, fontSize = 12.sp, color = AniTextSecondary)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onAddToWatchlist(pickedAnime) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, AniPurplePrimary)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdd,
                                        contentDescription = null,
                                        tint = AniPurplePrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = "Watchlist", color = Color.White, fontSize = 12.sp)
                                }

                                Button(
                                    onClick = { onAnimeClick(pickedAnime) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = AniCyanAccent)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "View Details",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
