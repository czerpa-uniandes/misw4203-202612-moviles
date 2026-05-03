package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
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
import com.team4.vinilosapp.data.repository.BandRepository
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
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.team4.vinilosapp.data.models.ArtistDetail

private class BandFakeAdapter : VinilosServiceAdapter {
    var bandsResponse: List<Performer> = emptyList()
    var musiciansResponse: List<Performer> = emptyList()
    var bandDetailResponse: BandDetail? = null
    var failBands = false
    var failMusicians = false
    var failBandDetail = false
    var failAddMusician = false

    override suspend fun getBands(): List<Performer> {
        if (failBands) throw Exception("bands error")
        return bandsResponse
    }

    override suspend fun getMusicians(): List<Performer> {
        if (failMusicians) throw Exception("musicians error")
        return musiciansResponse
    }

    override suspend fun getBandDetail(bandId: Int): BandDetail {
        if (failBandDetail) throw Exception("detail error")
        return bandDetailResponse ?: throw Exception("not found")
    }

    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) {
        if (failAddMusician) throw Exception("add musician error")
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
}

@OptIn(ExperimentalCoroutinesApi::class)
class BandViewModelTest {

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

    // fetchBands

    @Test
    fun fetchBands_updatesListOnSuccess() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(
                TestData.performer(id = 1, name = "Los Tupamaros"),
                TestData.performer(id = 2, name = "Carlos Vives y La Provincia")
            )
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchBands()
        advanceUntilIdle()

        assertEquals(2, viewModel.bands.value.size)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchBands_setsErrorMessageWhenAdapterFails() = runTest {
        val fakeAdapter = BandFakeAdapter().apply { failBands = true }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchBands()
        advanceUntilIdle()

        assertTrue(viewModel.bands.value.isEmpty())
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchBands_setsLoadingFalseAfterCompletion() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(TestData.performer())
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchBands()
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
    }

    // fetchBandDetail

    @Test
    fun fetchBandDetail_setsSelectedBandOnSuccess() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandDetailResponse = TestData.bandDetail(id = 5, name = "Los Tupamaros")
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchBandDetail(5)
        advanceUntilIdle()

        assertFalse(viewModel.detailLoading.value)
        assertNull(viewModel.detailError.value)
        assertEquals(5, viewModel.selectedBand.value?.id)
        assertEquals("Los Tupamaros", viewModel.selectedBand.value?.name)
    }

    @Test
    fun fetchBandDetail_setsDetailErrorWhenAdapterFails() = runTest {
        val fakeAdapter = BandFakeAdapter().apply { failBandDetail = true }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchBandDetail(5)
        advanceUntilIdle()

        assertFalse(viewModel.detailLoading.value)
        assertEquals("detail error", viewModel.detailError.value)
        assertNull(viewModel.selectedBand.value)
    }

    // fetchAllMusicians

    @Test
    fun fetchAllMusicians_updatesListOnSuccess() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Joe Arroyo"),
                TestData.performer(id = 2, name = "Carlos Vives")
            )
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchAllMusicians()
        advanceUntilIdle()

        assertEquals(2, viewModel.allMusicians.value.size)
        assertFalse(viewModel.musiciansLoading.value)
    }

    @Test
    fun fetchAllMusicians_keepsListEmptyWhenAdapterFails() = runTest {
        val fakeAdapter = BandFakeAdapter().apply { failMusicians = true }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))

        viewModel.fetchAllMusicians()
        advanceUntilIdle()

        assertTrue(viewModel.allMusicians.value.isEmpty())
        assertFalse(viewModel.musiciansLoading.value)
    }

    // addMusicianToBand

    @Test
    fun addMusicianToBand_callsOnSuccessAndRefreshesDetail() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandDetailResponse = TestData.bandDetail(id = 3, name = "Los Tupamaros")
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        var onSuccessCalled = false

        viewModel.addMusicianToBand(bandId = 3, musicianId = 7) { onSuccessCalled = true }
        advanceUntilIdle()

        assertTrue(onSuccessCalled)
        assertFalse(viewModel.addMusicianLoading.value)
        assertNull(viewModel.addMusicianError.value)
        assertEquals(3, viewModel.selectedBand.value?.id)
    }

    @Test
    fun addMusicianToBand_setsErrorWhenAdapterFails() = runTest {
        val fakeAdapter = BandFakeAdapter().apply { failAddMusician = true }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        var onSuccessCalled = false

        viewModel.addMusicianToBand(bandId = 3, musicianId = 7) { onSuccessCalled = true }
        advanceUntilIdle()

        assertFalse(onSuccessCalled)
        assertFalse(viewModel.addMusicianLoading.value)
        assertEquals("add musician error", viewModel.addMusicianError.value)
    }

    // search

    @Test
    fun search_filtersBandsByName() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(
                TestData.performer(id = 1, name = "Los Tupamaros"),
                TestData.performer(id = 2, name = "Carlos Vives y La Provincia")
            )
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        viewModel.fetchBands()
        advanceUntilIdle()

        viewModel.search("carlos")

        assertEquals(1, viewModel.bands.value.size)
        assertEquals("Carlos Vives y La Provincia", viewModel.bands.value.first().name)
    }

    @Test
    fun search_returnsAllBandsWhenQueryIsBlank() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(
                TestData.performer(id = 1, name = "Banda 1"),
                TestData.performer(id = 2, name = "Banda 2")
            )
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        viewModel.fetchBands()
        advanceUntilIdle()

        viewModel.search("banda 1")
        assertEquals(1, viewModel.bands.value.size)

        viewModel.search("")
        assertEquals(2, viewModel.bands.value.size)
    }

    @Test
    fun search_isNormalizedAndIgnoresAccents() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(TestData.performer(id = 1, name = "Héroe Sinfonía"))
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        viewModel.fetchBands()
        advanceUntilIdle()

        viewModel.search("heroe sinfonia")

        assertEquals(1, viewModel.bands.value.size)
        assertEquals("Héroe Sinfonía", viewModel.bands.value.first().name)
    }

    @Test
    fun search_isCaseInsensitive() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(TestData.performer(id = 1, name = "Los Tupamaros"))
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        viewModel.fetchBands()
        advanceUntilIdle()

        viewModel.search("TUPAMAROS")

        assertEquals(1, viewModel.bands.value.size)
    }

    @Test
    fun search_returnsEmptyWhenNoMatch() = runTest {
        val fakeAdapter = BandFakeAdapter().apply {
            bandsResponse = listOf(TestData.performer(id = 1, name = "Los Tupamaros"))
        }
        val viewModel = BandViewModel(application, BandRepository(fakeAdapter))
        viewModel.fetchBands()
        advanceUntilIdle()

        viewModel.search("beatles")

        assertTrue(viewModel.bands.value.isEmpty())
    }
}
