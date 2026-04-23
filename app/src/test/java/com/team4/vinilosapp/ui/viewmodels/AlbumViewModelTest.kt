package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import com.team4.vinilosapp.TestData
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.repository.AlbumRepository
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

private class FakeVinilosServiceAdapter : VinilosServiceAdapter {
    var albumsResponse: List<Album> = emptyList()
    var detailResponse: Album? = null
    var failAlbums = false
    var failDetail = false

    override suspend fun getAlbums(): List<Album> {
        if (failAlbums) throw Exception("albums error")
        return albumsResponse
    }

    override suspend fun getAlbumDetail(albumId: Int): Album {
        if (failDetail) throw Exception("detail error")
        return detailResponse ?: throw Exception("not found")
    }

    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
    override suspend fun getCollectors(): List<Collector> = emptyList()
}

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

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
    fun fetchAlbums_updatesAlbumsOnSuccess() = runTest {
        val fakeAdapter = FakeVinilosServiceAdapter().apply {
            albumsResponse = listOf(TestData.album(id = "1"), TestData.album(id = "2", name = "Otro álbum"))
        }
        val repository = AlbumRepository(fakeAdapter)
        val viewModel = AlbumViewModel(application, repository)

        viewModel.fetchAlbums()
        advanceUntilIdle()

        assertEquals(2, viewModel.albums.value.size)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchAlbums_returnsEmptyListOnFailure() = runTest {
        val fakeAdapter = FakeVinilosServiceAdapter().apply {
            failAlbums = true
        }
        val repository = AlbumRepository(fakeAdapter)
        val viewModel = AlbumViewModel(application, repository)

        viewModel.fetchAlbums()
        advanceUntilIdle()

        assertTrue(viewModel.albums.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun loadAlbum_setsSuccessOnSuccess() = runTest {
        val album = TestData.album()
        val fakeAdapter = FakeVinilosServiceAdapter().apply {
            detailResponse = album
        }
        val repository = AlbumRepository(fakeAdapter)
        val viewModel = AlbumViewModel(application, repository)

        viewModel.loadAlbum(1)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is AlbumDetailUiState.Success)
        assertEquals("Poeta del pueblo", (state as AlbumDetailUiState.Success).album.name)
    }

    @Test
    fun loadAlbum_setsErrorOnFailure() = runTest {
        val fakeAdapter = FakeVinilosServiceAdapter().apply {
            failDetail = true
        }
        val repository = AlbumRepository(fakeAdapter)
        val viewModel = AlbumViewModel(application, repository)

        viewModel.loadAlbum(999)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is AlbumDetailUiState.Error)
        assertEquals("detail error", (state as AlbumDetailUiState.Error).message)
    }
}