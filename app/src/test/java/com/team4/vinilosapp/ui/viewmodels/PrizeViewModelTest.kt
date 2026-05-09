package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
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
import com.team4.vinilosapp.data.repository.PrizeRepository
import com.team4.vinilosapp.ui.models.AddPrize
import com.team4.vinilosapp.ui.models.AddPrizeArtist
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class PrizeFakeServiceAdapter : VinilosServiceAdapter {

    var prizesResponse: List<Prize> = emptyList()
    var failGetPrizes = false
    var failCreatePrize = false
    var failAssociatePrize = false

    var receivedPrize: AddPrize? = null
    var receivedPrizeId: Int? = null
    var receivedArtistId: Int? = null
    var receivedPremiationDate: AddPrizeArtist? = null

    override suspend fun getPrizes(): List<Prize> {
        if (failGetPrizes) throw Exception("prizes error")
        return prizesResponse
    }

    override suspend fun addPrize(prize: AddPrize) {
        if (failCreatePrize) throw Exception("create prize error")
        receivedPrize = prize
    }

    override suspend fun associatePrizeArtist(
        prizeId: Int,
        artistId: Int,
        premiationDate: AddPrizeArtist
    ) {
        if (failAssociatePrize) throw Exception("associate prize error")

        receivedPrizeId = prizeId
        receivedArtistId = artistId
        receivedPremiationDate = premiationDate
    }

    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = throw NotImplementedError()
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
    override suspend fun getCollectors(): List<Collector> = emptyList()
    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = throw NotImplementedError()
    override suspend fun getBandDetail(bandId: Int): BandDetail = throw NotImplementedError()
    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) = Unit
    override suspend fun addComment(albumId: String, comment: AlbumCommentRequest): AlbumCommentResponse = throw NotImplementedError()
    override suspend fun addAlbumToMusician(musicianId: Int, albumId: Int) = Unit
    override suspend fun addAlbumToCollector(
        albumId: String,
        collectorId: String,
        albumToCollector: AddAlbumToCollectorRequest
    ): AddAlbumToCollectorResponse = throw NotImplementedError()
    override suspend fun getArtistDetail(artistId: Int): ArtistDetail = throw NotImplementedError()
}

@OptIn(ExperimentalCoroutinesApi::class)
class PrizeViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var application: Application

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        application = Mockito.mock(Application::class.java)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchPrizes_updatesPrizesOnSuccess() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter().apply {
            prizesResponse = listOf(
                Prize(
                    id = 1,
                    name = "Grammy Latino",
                    description = "Premio musical",
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

        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.fetchPrizes()
        advanceUntilIdle()

        assertEquals(2, viewModel.prizes.value.size)
        assertEquals("Grammy Latino", viewModel.prizes.value[0].name)
        assertFalse(viewModel.isLoading.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun fetchPrizes_setsErrorOnFailure() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter().apply {
            failGetPrizes = true
        }

        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.fetchPrizes()
        advanceUntilIdle()

        assertTrue(viewModel.prizes.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
        assertEquals("prizes error", viewModel.error.value)
    }

    @Test
    fun createPrize_setsSuccessOnSuccess() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter()
        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.createPrize(
            name = "Grammy Latino",
            description = "Premio al mejor",
            organization = "Academia"
        )

        advanceUntilIdle()

        assertFalse(viewModel.createLoading.value)
        assertTrue(viewModel.createSuccess.value)
        assertEquals(null, viewModel.createError.value)
        assertEquals("Grammy Latino", fakeAdapter.receivedPrize?.name)
        assertEquals("Premio al mejor", fakeAdapter.receivedPrize?.description)
        assertEquals("Academia", fakeAdapter.receivedPrize?.organization)
    }

    @Test
    fun createPrize_setsErrorOnFailure() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter().apply {
            failCreatePrize = true
        }

        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.createPrize(
            name = "Grammy Latino",
            description = "Premio al mejor",
            organization = "Academia"
        )

        advanceUntilIdle()

        assertFalse(viewModel.createLoading.value)
        assertFalse(viewModel.createSuccess.value)
        assertEquals("create prize error", viewModel.createError.value)
    }

    @Test
    fun associatePrizeArtist_setsSuccessOnSuccess() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter()
        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.associatePrizeArtist(
            prizeId = 10,
            artistId = 20,
            premiationDate = "1980-12-10"
        )

        advanceUntilIdle()

        assertFalse(viewModel.associateLoading.value)
        assertTrue(viewModel.associateSuccess.value)
        assertEquals(null, viewModel.associateError.value)
        assertEquals(10, fakeAdapter.receivedPrizeId)
        assertEquals(20, fakeAdapter.receivedArtistId)
        assertEquals(
            "1980-12-10T00:00:00.000Z",
            fakeAdapter.receivedPremiationDate?.premiationDate
        )
    }

    @Test
    fun associatePrizeArtist_setsErrorOnFailure() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter().apply {
            failAssociatePrize = true
        }

        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.associatePrizeArtist(
            prizeId = 10,
            artistId = 20,
            premiationDate = "1980-12-10"
        )

        advanceUntilIdle()

        assertFalse(viewModel.associateLoading.value)
        assertFalse(viewModel.associateSuccess.value)
        assertEquals("associate prize error", viewModel.associateError.value)
    }

    @Test
    fun resetCreateState_clearsCreateSuccessAndError() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter()
        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.createPrize(
            name = "MTV VMAs",
            description = "Premio al mejor",
            organization = "Academia"
        )

        advanceUntilIdle()

        viewModel.resetCreateState()

        assertFalse(viewModel.createSuccess.value)
        assertEquals(null, viewModel.createError.value)
    }

    @Test
    fun resetAssociateState_clearsAssociateSuccessAndError() = runTest {
        val fakeAdapter = PrizeFakeServiceAdapter()
        val repository = PrizeRepository(fakeAdapter)
        val viewModel = PrizeViewModel(application, repository)

        viewModel.associatePrizeArtist(
            prizeId = 10,
            artistId = 20,
            premiationDate = "1980-12-10"
        )

        advanceUntilIdle()

        viewModel.resetAssociateState()

        assertFalse(viewModel.associateSuccess.value)
        assertEquals(null, viewModel.associateError.value)
    }
}