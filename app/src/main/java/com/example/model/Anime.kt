package com.example.model

enum class AnimeFormat(val displayName: String) {
    TV("TV Series"),
    MOVIE("Movie"),
    OVA("OVA"),
    SPECIAL("Special")
}

enum class AnimeMood(
    val title: String,
    val icon: String,
    val subtitle: String,
    val colorHex: Long
) {
    HYPE("Hype & Action", "⚡", "Adrenaline, peak fights & shonen hype", 0xFFFF5722),
    PSYCHOLOGICAL("Mind-Bending", "🧠", "Twists, cat-and-mouse & thriller", 0xFF9C27B0),
    TEARJERKER("Emotional", "💧", "Heartfelt stories that make you cry", 0xFF2196F3),
    COZY("Cozy & Relaxing", "🍵", "Comfort food, slice of life & peace", 0xFF4CAF50),
    ROMANCE("Romance", "💖", "Wholesome chemistry & butterflies", 0xFFE91E63),
    FANTASY("Epic Fantasy", "🌌", "Expansive worlds, magic & adventure", 0xFF673AB7),
    DARK("Dark & Grim", "🌑", "Demons, dark fantasy & occult horrors", 0xFF3F51B5),
    COMEDY("Laugh Out Loud", "😂", "Pure humor, parody & daily chaos", 0xFFFFB300)
}

data class Anime(
    val id: Int,
    val title: String,
    val japaneseTitle: String,
    val englishTitle: String = title,
    val synopsis: String,
    val score: Double,
    val episodes: Int,
    val duration: String,
    val format: AnimeFormat = AnimeFormat.TV,
    val year: Int,
    val season: String = "All Time",
    val genres: List<String>,
    val studio: String,
    val coverUrl: String,
    val heroBannerUrl: String = coverUrl,
    val hook: String,
    val vibes: List<AnimeMood>,
    val tags: List<String>,
    val similarTitles: List<String> = emptyList(),
    val matchReasons: List<String> = emptyList(),
    val streamingOn: List<String> = listOf("Crunchyroll", "Netflix")
)
