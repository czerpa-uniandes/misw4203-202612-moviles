package com.team4.vinilosapp.data.repository

import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.AddAlbumToCollectorRequest
import com.team4.vinilosapp.data.models.AddAlbumToCollectorResponse
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.AlbumCommentRequest
import com.team4.vinilosapp.data.models.AlbumCommentResponse
import com.team4.vinilosapp.data.models.ArtistDetail
import com.team4.vinilosapp.data.models.BandDetail
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.CollectorDetail
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.models.Prize
import com.team4.vinilosapp.ui.models.AddPrize
import com.team4.vinilosapp.ui.models.AddPrizeArtist
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private class PrizeFakeAdapter : VinilosServiceAdapter {

    var prizesResponse: List<Prize> = emptyList()
    var failPrizes = false
    var failCreatePrize = false
    var failAssociatePrize = false

    var receivedPrize: AddPrize? = null
    var receivedPrizeId: Int? = null
    var receivedArtistId: Int? = null
    var receivedPremiationDate: AddPrizeArtist? = null

    override suspend fun getPrizes(): List<Prize> {
        if (failPrizes) throw Exception("prizes fail")
        return prizesResponse
    }

    override suspend fun addPrize(prize: AddPrize) {
        if (failCreatePrize) throw Exception("create prize fail")
        receivedPrize = prize
    }

    override suspend fun associatePrizeArtist(
        prizeId: Int,
        artistId: Int,
        premiationDate: AddPrizeArtist
    ) {
        if (failAssociatePrize) throw Exception("associate prize fail")

        receivedPrizeId = prizeId
        receivedArtistId = artistId
        receivedPremiationDate = premiationDate
    }

    override suspend fun getCollectors(): List<Collector> = emptyList()
    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = throw NotImplementedError()
    override suspend fun getBandDetail(bandId: Int): BandDetail = throw NotImplementedError()
    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) = Unit
    override suspend fun addAlbumToMusician(musicianId: Int, albumId: Int) = Unit
    override suspend fun addComment(
        albumId: String,
        comment: AlbumCommentRequest
    ): AlbumCommentResponse = throw NotImplementedError()

    override suspend fun addAlbumToCollector(
        albumId: String,
        collectorId: String,
        albumToCollector: AddAlbumToCollectorRequest
    ): AddAlbumToCollectorResponse = throw NotImplementedError()

    override suspend fun getArtistDetail(artistId: Int): ArtistDetail = throw NotImplementedError()
}

class PrizeRepositoryTest {

    @Test
    fun getPrizes_returnsSuccessWithData() = runBlocking {
        val adapter = PrizeFakeAdapter().apply {
            prizesResponse = listOf(
                Prize(
                    id = 1,
                    name = "Grammy Latino",
                    description = "Premio al mejor",
                    organization = "Academia",
                    performerPrizes = emptyList()
                ),
                Prize(
                    id = 2,
                    name = "Premios Lo Nuestro",
                    description = "Premio latino",
                    organization = "Univisión",
                    performerPrizes = emptyList()
                )
            )
        }

        val repository = PrizeRepository(adapter)

        val result = repository.getPrizes()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Grammy Latino", result.getOrNull()?.get(0)?.name)
    }

    @Test
    fun getPrizes_returnsEmptyListWhenResponseIsEmpty() = runBlocking {
        val repository = PrizeRepository(PrizeFakeAdapter())

        val result = repository.getPrizes()

        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    @Test
    fun getPrizes_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = PrizeFakeAdapter().apply {
            failPrizes = true
        }

        val repository = PrizeRepository(adapter)

        val result = repository.getPrizes()

        assertTrue(result.isFailure)
        assertEquals("prizes fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun createPrize_returnsSuccessAndSendsBody() = runBlocking {
        val adapter = PrizeFakeAdapter()
        val repository = PrizeRepository(adapter)

        val result = repository.createPrize(
            AddPrize(
                name = "Grammy Latino",
                description = "Premio al mejor",
                organization = "Academia"
            )
        )

        assertTrue(result.isSuccess)
        assertEquals("Grammy Latino", adapter.receivedPrize?.name)
        assertEquals("Premio al mejor", adapter.receivedPrize?.description)
        assertEquals("Academia", adapter.receivedPrize?.organization)
    }

    @Test
    fun createPrize_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = PrizeFakeAdapter().apply {
            failCreatePrize = true
        }

        val repository = PrizeRepository(adapter)

        val result = repository.createPrize(
            AddPrize(
                name = "Grammy Latino",
                description = "Premio al mejor",
                organization = "Academia"
            )
        )

        assertTrue(result.isFailure)
        assertEquals("create prize fail", result.exceptionOrNull()?.message)
    }

    @Test
    fun associatePrizeArtist_returnsSuccessAndSendsBody() = runBlocking {
        val adapter = PrizeFakeAdapter()
        val repository = PrizeRepository(adapter)

        val result = repository.associatePrizeArtist(
            prizeId = 10,
            artistId = 20,
            premiationDate = "1980-12-10T00:00:00.000Z"
        )

        assertTrue(result.isSuccess)
        assertEquals(10, adapter.receivedPrizeId)
        assertEquals(20, adapter.receivedArtistId)
        assertEquals(
            "1980-12-10T00:00:00.000Z",
            adapter.receivedPremiationDate?.premiationDate
        )
    }

    @Test
    fun associatePrizeArtist_returnsFailureWhenAdapterFails() = runBlocking {
        val adapter = PrizeFakeAdapter().apply {
            failAssociatePrize = true
        }

        val repository = PrizeRepository(adapter)

        val result = repository.associatePrizeArtist(
            prizeId = 10,
            artistId = 20,
            premiationDate = "1980-12-10T00:00:00.000Z"
        )

        assertTrue(result.isFailure)
        assertEquals("associate prize fail", result.exceptionOrNull()?.message)
    }
}