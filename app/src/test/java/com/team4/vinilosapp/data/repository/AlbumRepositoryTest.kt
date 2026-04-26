package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.TestData
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class RepositoryFakeAdapter : VinilosServiceAdapter {
    var albumsResponse: List<Album> = emptyList()
    var detailResponse: Album? = null
    var failAlbums = false
    var failDetail = false
    var failAddTrack = false
    var receivedAlbumId: Int? = null
    var receivedTrack: AddTrack? = null

    override suspend fun getAlbums(): List<Album> {
        if (failAlbums) throw Exception("albums fail")
        return albumsResponse
    }

    override suspend fun getAlbumDetail(albumId: Int): Album {
        if (failDetail) throw Exception("detail fail")
        return detailResponse ?: throw Exception("missing detail")
    }

    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) {
        if (failAddTrack) throw Exception("add track fail")

        receivedAlbumId = albumId
        receivedTrack = track
    }
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
    override suspend fun getCollectors(): List<Collector> = emptyList()
    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = throw NotImplementedError()
}

class AlbumRepositoryTest {

    @Test
    fun getAlbums_returnsSuccessWhenAdapterSucceeds() = runBlocking {
        val adapter = RepositoryFakeAdapter().apply {
            albumsResponse = listOf(TestData.album())
        }
        val repository = AlbumRepository(adapter)

        val result = repository.getAlbums()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun getAlbums_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = RepositoryFakeAdapter().apply {
            failAlbums = true
        }
        val repository = AlbumRepository(adapter)

        val result = repository.getAlbums()

        assertTrue(result.isFailure)
    }

    @Test
    fun getAlbumDetail_returnsSuccessWhenAdapterSucceeds() = runBlocking {
        val adapter = RepositoryFakeAdapter().apply {
            detailResponse = TestData.album()
        }
        val repository = AlbumRepository(adapter)

        val result = repository.getAlbumDetail(1)

        assertTrue(result.isSuccess)
        assertEquals("Poeta del pueblo", result.getOrNull()?.name)
    }

    @Test
    fun getAlbumDetail_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = RepositoryFakeAdapter().apply {
            failDetail = true
        }
        val repository = AlbumRepository(adapter)

        val result = repository.getAlbumDetail(1)

        assertTrue(result.isFailure)
    }

    @Test
    fun addTrack_returnsSuccessWhenAdapterSucceeds() = runBlocking {
        val adapter = RepositoryFakeAdapter()
        val repository = AlbumRepository(adapter)

        val track = AddTrack(
            name = "Pedro Navaja",
            duration = "5:20"
        )

        val result = repository.addTrack(
            albumId = 1,
            track = track
        )

        assertTrue(result.isSuccess)
        assertEquals(1, adapter.receivedAlbumId)
        assertEquals("Pedro Navaja", adapter.receivedTrack?.name)
        assertEquals("5:20", adapter.receivedTrack?.duration)
    }

    @Test
    fun addTrack_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = RepositoryFakeAdapter().apply {
            failAddTrack = true
        }
        val repository = AlbumRepository(adapter)

        val track = AddTrack(
            name = "Pedro Navaja",
            duration = "5:20"
        )

        val result = repository.addTrack(
            albumId = 1,
            track = track
        )

        assertTrue(result.isFailure)
    }
}