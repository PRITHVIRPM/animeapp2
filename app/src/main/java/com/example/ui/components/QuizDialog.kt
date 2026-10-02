package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Anime
import com.example.model.AnimeMood
import com.example.ui.theme.AniCardBorder
import com.example.ui.theme.AniCyanAccent
import com.example.ui.theme.AniDarkBg
import com.example.ui.theme.AniDarkSurface
import com.example.ui.theme.AniDarkSurfaceVariant
import com.example.ui.theme.AniPurplePrimary
import com.example.ui.theme.AniTextPrimary
import com.example.ui.theme.AniTextSecondary
import com.example.ui.viewmodel.QuizState

@Composable
fun QuizDialog(
    quizState: QuizState,
    onDismiss: () -> Unit,
    onSelectMood: (AnimeMood) -> Unit,
    onSelectFormat: (String) -> Unit,
    onSelectPacing: (String) -> Unit,
    onAnimeClick: (Anime) -> Unit
) {
    if (!quizState.isOpen) return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("quiz_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = AniDarkBg),
            border = BorderStroke(1.5.dp, AniPurplePrimary.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (quizState.step <= 3) "Anime Matcher (${quizState.step}/3)" else "🎉 Perfect Matches Found!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AniCyanAccent
                        )
                        Text(
                            text = when (quizState.step) {
                                1 -> "What's your mood tonight?"
                                2 -> "How much time do you have?"
                                3 -> "What tone do you crave?"
                                else -> "Curated Just For You"
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AniTextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Quiz",
                            tint = AniTextSecondary
                        )
                    }
                }

                if (quizState.step <= 3) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { quizState.step / 3f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = AniPurplePrimary,
                        trackColor = AniDarkSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(
                    targetState = quizState.step,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "quiz_steps"
                ) { step ->
                    when (step) {
                        1 -> StepMoodSelection(onSelectMood = onSelectMood)
                        2 -> StepFormatSelection(onSelectFormat = onSelectFormat)
                        3 -> StepPacingSelection(onSelectPacing = onSelectPacing)
                        else -> StepRecommendations(
                            recommendations = quizState.recommendations,
                            onAnimeClick = {
                                onDismiss()
                                onAnimeClick(it)
                            },
                            onClose = onDismiss
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepMoodSelection(onSelectMood: (AnimeMood) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimeMood.values().forEach { mood ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectMood(mood) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = mood.icon, fontSize = 24.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = mood.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AniTextPrimary
                        )
                        Text(
                            text = mood.subtitle,
                            fontSize = 11.sp,
                            color = AniTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepFormatSelection(onSelectFormat: (String) -> Unit) {
    val formats = listOf(
        Triple("SHORT", "⚡ 10-13 Episodes", "Perfect for a 1-weekend binge"),
        Triple("LONG", "🏰 24+ Episodes", "Deep expansive world and long character arcs"),
        Triple("MOVIE", "🎬 Feature Movie", "Complete cinematic journey in 2 hours"),
        Triple("ANY", "✨ Any Length", "I'm open to anything exceptional")
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        formats.forEach { (key, title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectFormat(key) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = desc, fontSize = 11.sp, color = AniTextSecondary)
                }
            }
        }
    }
}

@Composable
private fun StepPacingSelection(onSelectPacing: (String) -> Unit) {
    val pacings = listOf(
        Triple("FAST_HYPE", "🔥 Fast-Paced, Hype & Fights", "High adrenaline, action set-pieces, cliffhangers"),
        Triple("DEEP_EMOTIONAL", "🌧️ Deep, Emotional & Thoughtful", "Character psychology, heavy themes, tearjerkers"),
        Triple("COZY_COMEDY", "🌸 Warm, Cozy & Laughs", "Feel-good comfort, comedy, heartwarming chemistry")
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        pacings.forEach { (key, title, desc) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectPacing(key) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniCardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = AniTextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = desc, fontSize = 11.sp, color = AniTextSecondary)
                }
            }
        }
    }
}

@Composable
private fun StepRecommendations(
    recommendations: List<Anime>,
    onAnimeClick: (Anime) -> Unit,
    onClose: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        recommendations.forEach { anime ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnimeClick(anime) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = AniDarkSurface),
                border = BorderStroke(1.dp, AniPurplePrimary.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = anime.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = AniTextPrimary
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = anime.hook,
                            fontSize = 11.sp,
                            color = AniTextSecondary,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⭐ ${anime.score} • ${anime.studio} • ${anime.episodes} eps",
                            fontSize = 10.sp,
                            color = AniCyanAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AniPurplePrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Explore More Anime", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
