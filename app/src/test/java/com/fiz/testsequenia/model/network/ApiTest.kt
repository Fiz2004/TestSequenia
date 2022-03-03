package com.fiz.testsequenia.model.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiTest {

    val mockWebServer = MockWebServer()
    private lateinit var service: MoviesApiService

    fun enqueue(fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val buffer = inputStream.source().buffer()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(buffer.readString(Charsets.UTF_8))
        )
    }

    @Before
    fun setup() {
        val url = mockWebServer.url("/")
        service = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .build()
            .create(MoviesApiService::class.java)
    }

    @Test
    fun api_service() {
        enqueue("Movies_property.json")
        runBlocking {
            val apiResponse = service.getProperties()
            assertNotNull(apiResponse)
            assertTrue("The list was empty", apiResponse.films.isNotEmpty())
            assertEquals("The IDs did not match", 326, apiResponse.films[0].id)
        }
    }
}