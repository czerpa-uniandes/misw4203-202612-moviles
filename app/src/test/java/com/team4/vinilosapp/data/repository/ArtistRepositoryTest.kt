package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.TestData
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.AddAlbumToCollectorRequest
import com.team4.vinilosapp.data.models.AddAlbumToCollectorResponse
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.AlbumCommentRequest
import com.team4.vinilosapp.data.models.AlbumCommentResponse
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.team4.vinilosapp.data.models.ArtistDetail

private class ArtistFakeAdapter : VinilosServiceAdapter {
    var musiciansResponse: List<Performer> = emptyList()
    var bandsResponse: List<Performer> = emptyList()
    var failMusicians = false
    var failBands = false

    override suspend fun getMusicians(): List<Performer> {
        if (failMusicians) throw Exception("musicians fail")
        return musiciansResponse
    }

    override suspend fun getBands(): List<Performer> {
        if (failBands) throw Exception("bands fail")
        return bandsResponse
    }

    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getCollectors(): List<Collector> = emptyList()
    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = throw NotImplementedError()
    override suspend fun getBandDetail(bandId: Int): BandDetail = throw NotImplementedError()
    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) = Unit
    override suspend fun addComment(albumId: String, comment: AlbumCommentRequest): AlbumCommentResponse = throw NotImplementedError()
    override suspend fun addAlbumToCollector(albumId: String, collectorId: String, albumToCollector: AddAlbumToCollectorRequest): AddAlbumToCollectorResponse = throw NotImplementedError()
    override suspend fun getArtistDetail(artistId: Int): ArtistDetail = throw NotImplementedError()
}

class ArtistRepositoryTest {

    @Test
    fun getArtists_returnsMusicians() = runBlocking {
        val adapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Músico A"),
                TestData.performer(id = 2, name = "Músico B")
            )
        }
        val repository = ArtistRepository(adapter)

        val result = repository.getArtists()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Músico A", result.getOrNull()?.get(0)?.name)
        assertEquals("Músico B", result.getOrNull()?.get(1)?.name)
    }

    @Test
    fun getArtists_returnsOnlyMusiciansWhenBandsAreEmpty() = runBlocking {
        val adapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Músico 1"),
                TestData.performer(id = 2, name = "Músico 2")
            )
        }
        val repository = ArtistRepository(adapter)

        val result = repository.getArtists()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun getArtists_returnsEmptyListWhenBothEndpointsAreEmpty() = runBlocking {
        val repository = ArtistRepository(ArtistFakeAdapter())

        val result = repository.getArtists()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun getArtists_returnsFailureWhenMusiciansCallFails() = runBlocking {
        val adapter = ArtistFakeAdapter().apply { failMusicians = true }
        val repository = ArtistRepository(adapter)

        val result = repository.getArtists()

        assertTrue(result.isFailure)
        assertEquals("musicians fail", result.exceptionOrNull()?.message)
    }

}
