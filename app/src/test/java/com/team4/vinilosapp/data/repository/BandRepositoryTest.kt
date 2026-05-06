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
import com.team4.vinilosapp.ui.models.AddPrize

private class BandFakeAdapter : VinilosServiceAdapter {
    var bandsResponse: List<Performer> = emptyList()
    var musiciansResponse: List<Performer> = emptyList()
    var bandDetailResponse: BandDetail? = null
    var failBands = false
    var failMusicians = false
    var failBandDetail = false
    var failAddMusician = false
    var receivedBandId: Int? = null
    var receivedMusicianId: Int? = null

    override suspend fun getBands(): List<Performer> {
        if (failBands) throw Exception("bands fail")
        return bandsResponse
    }

    override suspend fun getMusicians(): List<Performer> {
        if (failMusicians) throw Exception("musicians fail")
        return musiciansResponse
    }

    override suspend fun getBandDetail(bandId: Int): BandDetail {
        if (failBandDetail) throw Exception("detail fail")
        return bandDetailResponse ?: throw Exception("not found")
    }

    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) {
        if (failAddMusician) throw Exception("add musician fail")
        receivedBandId = bandId
        receivedMusicianId = musicianId
    }

    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getCollectors(): List<Collector> = emptyList()
    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = throw NotImplementedError()
    override suspend fun addComment(albumId: String, comment: AlbumCommentRequest): AlbumCommentResponse = throw NotImplementedError()
    override suspend fun addAlbumToCollector(albumId: String, collectorId: String, albumToCollector: AddAlbumToCollectorRequest): AddAlbumToCollectorResponse = throw NotImplementedError()
    override suspend fun getArtistDetail(artistId: Int): ArtistDetail = throw NotImplementedError()
    override suspend fun addPrize(prize: AddPrize): Unit  = throw NotImplementedError()
}

class BandRepositoryTest {

    @Test
    fun getBands_returnsSuccessWithData() = runBlocking {
        val adapter = BandFakeAdapter().apply {
            bandsResponse = listOf(
                TestData.performer(id = 1, name = "Los Tupamaros"),
                TestData.performer(id = 2, name = "Carlos Vives y La Provincia")
            )
        }
        val repository = BandRepository(adapter)

        val result = repository.getBands()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Los Tupamaros", result.getOrNull()?.get(0)?.name)
    }

    @Test
    fun getBands_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        val repository = BandRepository(BandFakeAdapter())

        val result = repository.getBands()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun getBands_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = BandFakeAdapter().apply { failBands = true }
        val repository = BandRepository(adapter)

        val result = repository.getBands()

        assertTrue(result.isFailure)
        assertEquals("bands fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun getMusicians_returnsSuccessWithData() = runBlocking {
        val adapter = BandFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Joe Arroyo"),
                TestData.performer(id = 2, name = "Carlos Vives")
            )
        }
        val repository = BandRepository(adapter)

        val result = repository.getMusicians()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Joe Arroyo", result.getOrNull()?.get(0)?.name)
    }

    @Test
    fun getMusicians_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = BandFakeAdapter().apply { failMusicians = true }
        val repository = BandRepository(adapter)

        val result = repository.getMusicians()

        assertTrue(result.isFailure)
        assertEquals("musicians fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun getBandDetail_returnsSuccessWithData() = runBlocking {
        val musician = TestData.performer(id = 10, name = "Joe Arroyo")
        val adapter = BandFakeAdapter().apply {
            bandDetailResponse = TestData.bandDetail(id = 5, name = "Los Tupamaros", musicians = listOf(musician))
        }
        val repository = BandRepository(adapter)

        val result = repository.getBandDetail(5)

        assertTrue(result.isSuccess)
        assertEquals(5, result.getOrNull()?.id)
        assertEquals("Los Tupamaros", result.getOrNull()?.name)
        assertEquals(1, result.getOrNull()?.musicians?.size)
        assertEquals("Joe Arroyo", result.getOrNull()?.musicians?.first()?.name)
    }

    @Test
    fun getBandDetail_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = BandFakeAdapter().apply { failBandDetail = true }
        val repository = BandRepository(adapter)

        val result = repository.getBandDetail(5)

        assertTrue(result.isFailure)
        assertEquals("detail fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun addMusicianToBand_returnsSuccessAndForwardsIds() = runBlocking {
        val adapter = BandFakeAdapter()
        val repository = BandRepository(adapter)

        val result = repository.addMusicianToBand(bandId = 3, musicianId = 7)

        assertTrue(result.isSuccess)
        assertEquals(3, adapter.receivedBandId)
        assertEquals(7, adapter.receivedMusicianId)
    }

    @Test
    fun addMusicianToBand_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = BandFakeAdapter().apply { failAddMusician = true }
        val repository = BandRepository(adapter)

        val result = repository.addMusicianToBand(bandId = 3, musicianId = 7)

        assertTrue(result.isFailure)
        assertEquals("add musician fail", result.exceptionOrNull()?.message)
    }
}
