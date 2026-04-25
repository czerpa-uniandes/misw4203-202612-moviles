package com.team4.vinilosapp.data.adapters

import com.squareup.moshi.Moshi
import com.team4.vinilosapp.data.network.VinilosApiService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class VinilosServiceAdapterImplTest {

    private lateinit var server: MockWebServer
    private lateinit var adapter: VinilosServiceAdapterImpl

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()

        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        val api = retrofit.create(VinilosApiService::class.java)
        adapter = VinilosServiceAdapterImpl(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getAlbums_parsesResponseCorrectly() = runBlocking {
        val body = """
            [
              {
                "id": "1",
                "name": "Poeta del pueblo",
                "cover": "https://example.com/cover.jpg",
                "releaseDate": "2020-01-01T00:00:00Z",
                "description": "Descripción",
                "genre": "Rock",
                "recordLabel": "Sony Music",
                "tracks": [],
                "performers": [],
                "comments": []
              }
            ]
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getAlbums()

        assertEquals(1, result.size)
        assertEquals("Poeta del pueblo", result.first().name)
    }

    @Test
    fun getAlbumDetail_parsesResponseCorrectly() = runBlocking {
        val body = """
            {
              "id": "1",
              "name": "Poeta del pueblo",
              "cover": "https://example.com/cover.jpg",
              "releaseDate": "2020-01-01T00:00:00Z",
              "description": "Descripción",
              "genre": "Rock",
              "recordLabel": "Sony Music",
              "tracks": [],
              "performers": [],
              "comments": []
            }
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getAlbumDetail(1)

        assertEquals("1", result.id)
        assertEquals("Poeta del pueblo", result.name)
    }

    @Test
    fun getAlbumDetail_throwsOnHttpError(): Unit = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(404)
                .setBody("""{"message":"Not found"}""")
                .addHeader("Content-Type", "application/json")
        )

        assertFailsWith<Exception> {
            adapter.getAlbumDetail(999)
        }
    }

    @Test
    fun getMusicians_parsesResponseCorrectly() = runBlocking {
        val body = """
            [
              {
                "id": 1,
                "name": "Joe Arroyo",
                "image": "https://example.com/joe.jpg",
                "description": "Leyenda de la salsa",
                "birthDate": "1955-11-01T00:00:00Z"
              }
            ]
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getMusicians()

        assertEquals(1, result.size)
        assertEquals("Joe Arroyo", result.first().name)
        assertEquals(1, result.first().id)
    }

    @Test
    fun getMusicians_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]")
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getMusicians()

        assertEquals(0, result.size)
    }

    @Test
    fun getBands_parsesResponseCorrectly() = runBlocking {
        val body = """
            [
              {
                "id": 2,
                "name": "Los Tupamaros",
                "image": "https://example.com/banda.jpg",
                "description": "Banda colombiana",
                "creationDate": "1980-01-01T00:00:00Z"
              }
            ]
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getBands()

        assertEquals(1, result.size)
        assertEquals("Los Tupamaros", result.first().name)
        assertEquals(2, result.first().id)
    }

    @Test
    fun getBands_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]")
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getBands()

        assertEquals(0, result.size)
    }

    @Test
    fun getCollectors_parsesResponseCorrectly() = runBlocking {
        val body = """
            [
              {
                "id": 1,
                "name": "Elena Restrepo",
                "telephone": "3001234567",
                "email": "elena@example.com",
                "image": "https://example.com/elena.jpg"
              },
              {
                "id": 2,
                "name": "Carlos Mario",
                "telephone": "3109876543",
                "email": "carlos@example.com",
                "image": "https://example.com/carlos.jpg"
              }
            ]
        """.trimIndent()

        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getCollectors()

        assertEquals(2, result.size)
        assertEquals("Elena Restrepo", result.first().name)
        assertEquals("carlos@example.com", result.last().email)
    }

    @Test
    fun getCollectors_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("[]")
                .addHeader("Content-Type", "application/json")
        )

        val result = adapter.getCollectors()

        assertEquals(0, result.size)
    }

    @Test
    fun getCollectors_throwsOnHttpError(): Unit = runBlocking {
        server.enqueue(
            MockResponse()
                .setResponseCode(500)
                .setBody("""{"message":"Internal Server Error"}""")
                .addHeader("Content-Type", "application/json")
        )

        assertFailsWith<Exception> {
            adapter.getCollectors()
        }
    }
}