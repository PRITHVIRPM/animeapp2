package com.example.data.api

import com.example.model.Anime
import com.example.model.AnimeFormat
import com.example.model.AnimeMood
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JikanAnimeListResponse(
    @Json(name = "data") val data: List<JikanAnimeDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class JikanSingleAnimeResponse(
    @Json(name = "data") val data: JikanAnimeDto
)

@JsonClass(generateAdapter = true)
data class JikanAnimeDto(
    @Json(name = "malId") val malId: Int? = null,
    @Json(name = "mal_id") val mal_id: Int? = null,
    @Json(name = "title") val title: String? = null,
    @Json(name = "titleEnglish") val titleEnglish: String? = null,
    @Json(name = "title_english") val title_english: String? = null,
    @Json(name = "titleJapanese") val titleJapanese: String? = null,
    @Json(name = "title_japanese") val title_japanese: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "episodes") val episodes: Int? = null,
    @Json(name = "status") val status: String? = null,
    @Json(name = "duration") val duration: String? = null,
    @Json(name = "score") val score: Double? = null,
    @Json(name = "rank") val rank: Int? = null,
    @Json(name = "popularity") val popularity: Int? = null,
    @Json(name = "synopsis") val synopsis: String? = null,
    @Json(name = "year") val year: Int? = null,
    @Json(name = "season") val season: String? = null,
    @Json(name = "studios") val studios: List<JikanNamedItemDto>? = null,
    @Json(name = "genres") val genres: List<JikanNamedItemDto>? = null
)

@JsonClass(generateAdapter = true)
data class JikanNamedItemDto(
    @Json(name = "malId") val malId: Int? = null,
    @Json(name = "mal_id") val mal_id: Int? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "name") val name: String? = "",
    @Json(name = "url") val url: String? = null
)

fun JikanAnimeDto.toAnime(): Anime {
    val id = malId ?: mal_id ?: (title?.hashCode()?.let { kotlin.math.abs(it) } ?: 99999)
    val chosenTitle = titleEnglish ?: title_english ?: title ?: "Unknown Anime"
    val cover = imageUrl ?: ""

    val genreList = (genres?.mapNotNull { it.name } ?: emptyList())
    val studioName = studios?.firstOrNull()?.name ?: "Anime Studio"

    val animeFormat = when (type?.uppercase()) {
        "MOVIE" -> AnimeFormat.MOVIE
        "OVA" -> AnimeFormat.OVA
        "SPECIAL" -> AnimeFormat.SPECIAL
        else -> AnimeFormat.TV
    }

    // Determine vibes from genres or synopsis
    val derivedVibes = mutableListOf<AnimeMood>()
    val searchBlob = "${genreList.joinToString()} ${synopsis ?: ""}".lowercase()
    if (searchBlob.contains("action") || searchBlob.contains("adventure") || searchBlob.contains("shounen")) {
        derivedVibes.add(AnimeMood.HYPE)
    }
    if (searchBlob.contains("comedy") || searchBlob.contains("funny") || searchBlob.contains("parody")) {
        derivedVibes.add(AnimeMood.COMEDY)
    }
    if (searchBlob.contains("drama") || searchBlob.contains("tragedy") || searchBlob.contains("emotional")) {
        derivedVibes.add(AnimeMood.TEARJERKER)
    }
    if (searchBlob.contains("romance") || searchBlob.contains("love")) {
        derivedVibes.add(AnimeMood.ROMANCE)
    }
    if (searchBlob.contains("fantasy") || searchBlob.contains("magic") || searchBlob.contains("demon")) {
        derivedVibes.add(AnimeMood.FANTASY)
    }
    if (searchBlob.contains("slice of life") || searchBlob.contains("cozy") || searchBlob.contains("school")) {
        derivedVibes.add(AnimeMood.COZY)
    }
    if (searchBlob.contains("suspense") || searchBlob.contains("mystery") || searchBlob.contains("psychological") || searchBlob.contains("thriller")) {
        derivedVibes.add(AnimeMood.PSYCHOLOGICAL)
    }
    if (derivedVibes.isEmpty()) {
        derivedVibes.add(AnimeMood.HYPE)
    }

    val cleanSynopsis = synopsis?.replace("[Written by MAL Rewrite]", "")?.trim()
        ?.ifEmpty { "No synopsis available." }
        ?: "No synopsis available."

    return Anime(
        id = id,
        title = chosenTitle,
        japaneseTitle = titleJapanese ?: title_japanese ?: chosenTitle,
        englishTitle = chosenTitle,
        synopsis = cleanSynopsis,
        score = score ?: 0.0,
        episodes = episodes ?: 12,
        duration = duration ?: "24 min/ep",
        format = animeFormat,
        year = year ?: 2024,
        season = season?.replaceFirstChar { it.uppercase() } ?: "TV",
        genres = if (genreList.isNotEmpty()) genreList else listOf("Anime"),
        studio = studioName,
        coverUrl = cover,
        hook = cleanSynopsis.take(130).let { if (it.length >= 130) "$it..." else it },
        vibes = derivedVibes,
        tags = (if (score != null && score >= 8.5) listOf("Top Rated") else emptyList()) + (if (episodes != null && episodes > 24) listOf("Long Series") else listOf("Bingeable")),
        similarTitles = emptyList(),
        matchReasons = listOf("Real-time live result from MyAnimeList Jikan API")
    )
}
