package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.api.JikanApiClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AniSuggest", appName)
  }

  @Test
  fun `test Jikan API top anime call`() = runBlocking {
    val response = JikanApiClient.apiService.getTopAnime()
    println("TEST DEBUG: Top Anime Count = ${response.data.size}")
    assertTrue(response.data.isNotEmpty())
  }

  @Test
  fun `test Jikan API search call`() = runBlocking {
    val response = JikanApiClient.apiService.searchAnime("naruto")
    println("TEST DEBUG: Search Count = ${response.data.size}")
    assertTrue(response.data.isNotEmpty())
  }

  @Test
  fun `test Jikan API seasonal call`() = runBlocking {
    try {
      val response = JikanApiClient.apiService.getSeasonalAnime()
      println("TEST DEBUG: Seasonal Count = ${response.data.size}")
      assertTrue(response.data.isNotEmpty())
    } catch (e: Exception) {
      println("TEST ERROR IN getSeasonalAnime: ${e::class.java.name}: ${e.message}")
      e.printStackTrace()
      throw e
    }
  }
}
