package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.AnimeCatalog
import com.example.model.Anime
import com.example.ui.components.AnimePosterImage
import com.example.ui.components.ScoreBadge
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.viewmodel.UiState

@Composable
fun SimilarMatcherScreen(
    uiState: UiState,
    onSelectBaseAnime: (Anime) -> Unit,
    onAnimeClick: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {
    val base = uiState.matcherBaseAnime

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("similar_matcher_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "If You Loved... 🔗",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AniTextPrimary
                )
                Text(
                    text = "Pick a show you adored to discover kindred spirits with matching DNA",
                    fontSize = 13.sp,
                    color = AniTextSecondary
                )
            }
        }

        // Horizontal picker of shows
        item {
            Text(
                text = "Select Loved Anime:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AniTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(AnimeCatalog.allAnime.take(12)) { anime ->
                    val isSelected = base?.id == anime.id
                    Card(
                        modifier = Modifier
                            .width(100.dp)
                            .clickable { onSelectBaseAnime(anime) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                        border = BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) AniCyanAccent else AniCardBorder
                        )
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                            ) {
                                AnimePosterImage(
                                    anime = anime,
                                    modifier = Modifier.fillMaxSize()
                                )
                                if (isSelected) {
                                    Surface(
                                        color = AniCyanAccent,
                                        shape = RoundedCornerShape(bottomEnd = 8.dp),
                                        modifier = Modifier.align(Alignment.TopStart)
                                    ) {
                                        Text(
                                            text = "SELECTED",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = anime.title,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) AniCyanAccent else AniTextPrimary,
                                maxLines = 1,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }

        if (base != null) {
            item {
                Surface(
                    color = AniPurplePrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = AniCyanAccent
                        )
                        Column {
                            Text(
                                text = "Because you loved ${base.title}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Matching themes: ${base.genres.joinToString(", ")}",
                                fontSize = 11.sp,
                                color = AniTextSecondary
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Top Recommended Matches (${uiState.similarResults.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = AniTextPrimary
                )
            }

            items(uiState.similarResults) { match ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAnimeClick(match) },
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
                                .width(70.dp)
                                .height(95.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            AnimePosterImage(
                                anime = match,
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
                                    text = match.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AniTextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                ScoreBadge(score = match.score)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = match.hook,
                                fontSize = 11.sp,
                                color = AniTextSecondary,
                                maxLines = 2
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = AniDarkSurfaceVariant,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = match.studio,
                                        fontSize = 10.sp,
                                        color = AniPurplePrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    color = AniCyanAccent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "High Similarity",
                                        fontSize = 10.sp,
                                        color = AniCyanAccent,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = AniTextSecondary
                        )
                    }
                }
            }
        }
    }
}
