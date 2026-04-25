package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.TestData
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class CollectorFakeAdapter : VinilosServiceAdapter {
    var collectorsResponse: List<Collector> = emptyList()
    var failCollectors = false

    override suspend fun getCollectors(): List<Collector> {
        if (failCollectors) throw Exception("collectors fail")
        return collectorsResponse
    }

    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
}

class CollectorRepositoryTest {

    @Test
    fun getCollectors_returnsSuccessWithData() = runBlocking {
        val adapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(
                TestData.collector(id = 1, name = "Elena Restrepo"),
                TestData.collector(id = 2, name = "Carlos Mario")
            )
        }
        val repository = CollectorRepository(adapter)

        val result = repository.getCollectors()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Elena Restrepo", result.getOrNull()?.get(0)?.name)
    }

    @Test
    fun getCollectors_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        val repository = CollectorRepository(CollectorFakeAdapter())

        val result = repository.getCollectors()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun getCollectors_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = CollectorFakeAdapter().apply { failCollectors = true }
        val repository = CollectorRepository(adapter)

        val result = repository.getCollectors()

        assertTrue(result.isFailure)
        assertEquals("collectors fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun getCollectors_preservesCollectorFields() = runBlocking {
        val adapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(
                TestData.collector(
                    id = 5,
                    name = "Sofía Gómez",
                    telephone = "3101234567",
                    email = "sofia@example.com",
                    image = "https://example.com/sofia.jpg"
                )
            )
        }
        val repository = CollectorRepository(adapter)

        val result = repository.getCollectors()
        val collector = result.getOrNull()?.first()

        assertEquals(5, collector?.id)
        assertEquals("Sofía Gómez", collector?.name)
        assertEquals("3101234567", collector?.telephone)
        assertEquals("sofia@example.com", collector?.email)
        assertEquals("https://example.com/sofia.jpg", collector?.image)
    }
}
