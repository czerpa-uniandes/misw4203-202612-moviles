package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import com.team4.vinilosapp.TestData
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
import com.team4.vinilosapp.data.repository.ArtistRepository
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

private class ArtistFakeAdapter : VinilosServiceAdapter {
    var musiciansResponse: List<Performer> = emptyList()
    var bandsResponse: List<Performer> = emptyList()
    var failMusicians = false
    var failBands = false

    override suspend fun getMusicians(): List<Performer> {
        if (failMusicians) throw Exception("musicians error")
        return musiciansResponse
    }

    override suspend fun getBands(): List<Performer> {
        if (failBands) throw Exception("bands error")
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
    override suspend fun addPrize(prize: AddPrize): Unit = throw NotImplementedError()
    override suspend fun getPrizes(): List<Prize> = emptyList()
    override suspend fun associatePrizeArtist(prizeId: Int, artistId: Int, premiationDate: AddPrizeArtist) = throw NotImplementedError()
}

@OptIn(ExperimentalCoroutinesApi::class)
class ArtistViewModelTest {

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
    fun fetchArtists_updatesListOnSuccess() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Músico A"),
                TestData.performer(id = 2, name = "Músico B")
            )
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)

        viewModel.fetchArtists()
        advanceUntilIdle()

        assertEquals(2, viewModel.artists.value.size)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchArtists_setsEmptyListWhenAdapterFails() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply { failMusicians = true }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)

        viewModel.fetchArtists()
        advanceUntilIdle()

        assertTrue(viewModel.artists.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchArtists_setsLoadingFalseAfterCompletion() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(TestData.performer())
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)

        viewModel.fetchArtists()
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun search_filtersArtistsByName() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Joe Arroyo"),
                TestData.performer(id = 2, name = "Carlos Vives")
            )
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)
        viewModel.fetchArtists()
        advanceUntilIdle()

        viewModel.search("carlos")
        advanceUntilIdle()

        assertEquals(1, viewModel.artists.value.size)
        assertEquals("Carlos Vives", viewModel.artists.value.first().name)
    }

    @Test
    fun search_returnsAllArtistsWhenQueryIsBlank() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(
                TestData.performer(id = 1, name = "Artista 1"),
                TestData.performer(id = 2, name = "Artista 2")
            )
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)
        viewModel.fetchArtists()
        advanceUntilIdle()

        viewModel.search("artista 1")
        advanceUntilIdle()
        assertEquals(1, viewModel.artists.value.size)

        viewModel.search("")
        advanceUntilIdle()
        assertEquals(2, viewModel.artists.value.size)
    }

    @Test
    fun search_isNormalizedAndIgnoresAccents() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(TestData.performer(id = 1, name = "Charly García"))
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)
        viewModel.fetchArtists()
        advanceUntilIdle()

        viewModel.search("garcia")
        advanceUntilIdle()

        assertEquals(1, viewModel.artists.value.size)
        assertEquals("Charly García", viewModel.artists.value.first().name)
    }

    @Test
    fun search_isCaseInsensitive() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(TestData.performer(id = 1, name = "Miles Davis"))
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)
        viewModel.fetchArtists()
        advanceUntilIdle()

        viewModel.search("MILES")
        advanceUntilIdle()

        assertEquals(1, viewModel.artists.value.size)
    }

    @Test
    fun search_returnsEmptyWhenNoMatch() = runTest {
        val fakeAdapter = ArtistFakeAdapter().apply {
            musiciansResponse = listOf(TestData.performer(id = 1, name = "Nina Simone"))
        }
        val viewModel = ArtistViewModel(application, ArtistRepository(fakeAdapter), dispatcher)
        viewModel.fetchArtists()
        advanceUntilIdle()

        viewModel.search("beethoven")
        advanceUntilIdle()

        assertTrue(viewModel.artists.value.isEmpty())
    }
}