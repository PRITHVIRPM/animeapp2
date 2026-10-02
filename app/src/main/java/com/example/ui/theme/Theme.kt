package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AniPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = AniDarkSurfaceVariant,
    onPrimaryContainer = Color.White,
    secondary = AniCyanAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E3A5F),
    onSecondaryContainer = AniCyanAccent,
    tertiary = AniCoralTertiary,
    onTertiary = Color.White,
    background = AniDarkBg,
    onBackground = AniTextPrimary,
    surface = AniDarkSurface,
    onSurface = AniTextPrimary,
    surfaceVariant = AniDarkSurfaceVariant,
    onSurfaceVariant = AniTextSecondary,
    outline = AniCardBorder
)

private val LightColorScheme = DarkColorScheme // Anime aesthetic shines best in rich dark/twilight mode

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
