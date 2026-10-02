package com.example.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String
    ): JikanAnimeListResponse

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int? = null
    ): JikanAnimeListResponse

    @GET("seasons/now")
    suspend fun getSeasonalAnime(
        @Query("page") page: Int? = null
    ): JikanAnimeListResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Int
    ): JikanSingleAnimeResponse
}
