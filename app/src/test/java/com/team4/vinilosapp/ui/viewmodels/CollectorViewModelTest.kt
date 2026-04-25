package com.team4.vinilosapp.ui.viewmodels

import android.app.Application
import com.team4.vinilosapp.TestData
import com.team4.vinilosapp.data.adapters.VinilosServiceAdapter
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Collector
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.repository.CollectorRepository
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

private class CollectorFakeAdapter : VinilosServiceAdapter {
    var collectorsResponse: List<Collector> = emptyList()
    var failCollectors = false

    override suspend fun getCollectors(): List<Collector> {
        if (failCollectors) throw Exception("collectors error")
        return collectorsResponse
    }

    override suspend fun getAlbums(): List<Album> = emptyList()
    override suspend fun getAlbumDetail(albumId: Int): Album = throw NotImplementedError()
    override suspend fun createAlbum(album: NewAlbum) = Unit
    override suspend fun addTrack(albumId: Int, track: AddTrack) = Unit
    override suspend fun getMusicians(): List<Performer> = emptyList()
    override suspend fun getBands(): List<Performer> = emptyList()
}

@OptIn(ExperimentalCoroutinesApi::class)
class CollectorViewModelTest {

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
    fun fetchCollectors_updatesCollectorsOnSuccess() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(
                TestData.collector(id = 1, name = "Elena Restrepo"),
                TestData.collector(id = 2, name = "Carlos Mario")
            )
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))

        viewModel.fetchCollectors()
        advanceUntilIdle()

        assertEquals(2, viewModel.collectors.value.size)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchCollectors_setsEmptyListWhenAdapterFails() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply { failCollectors = true }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))

        viewModel.fetchCollectors()
        advanceUntilIdle()

        assertTrue(viewModel.collectors.value.isEmpty())
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun fetchCollectors_setsLoadingFalseAfterCompletion() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(TestData.collector())
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))

        viewModel.fetchCollectors()
        advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun search_filtersCollectorsByName() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(
                TestData.collector(id = 1, name = "Elena Restrepo"),
                TestData.collector(id = 2, name = "Carlos Mario")
            )
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))
        viewModel.fetchCollectors()
        advanceUntilIdle()

        viewModel.search("carlos")

        assertEquals(1, viewModel.collectors.value.size)
        assertEquals("Carlos Mario", viewModel.collectors.value.first().name)
    }

    @Test
    fun search_returnsAllCollectorsWhenQueryIsBlank() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(
                TestData.collector(id = 1, name = "Coleccionista 1"),
                TestData.collector(id = 2, name = "Coleccionista 2")
            )
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))
        viewModel.fetchCollectors()
        advanceUntilIdle()

        viewModel.search("coleccionista 1")
        assertEquals(1, viewModel.collectors.value.size)

        viewModel.search("")
        assertEquals(2, viewModel.collectors.value.size)
    }

    @Test
    fun search_isNormalizedAndIgnoresAccents() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(TestData.collector(id = 1, name = "Sofía Gómez"))
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))
        viewModel.fetchCollectors()
        advanceUntilIdle()

        viewModel.search("sofia gomez")

        assertEquals(1, viewModel.collectors.value.size)
        assertEquals("Sofía Gómez", viewModel.collectors.value.first().name)
    }

    @Test
    fun search_isCaseInsensitive() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(TestData.collector(id = 1, name = "Andrés Pardo"))
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))
        viewModel.fetchCollectors()
        advanceUntilIdle()

        viewModel.search("ANDRES")

        assertEquals(1, viewModel.collectors.value.size)
    }

    @Test
    fun search_returnsEmptyWhenNoMatch() = runTest {
        val fakeAdapter = CollectorFakeAdapter().apply {
            collectorsResponse = listOf(TestData.collector(id = 1, name = "Julián Velez"))
        }
        val viewModel = CollectorViewModel(application, CollectorRepository(fakeAdapter))
        viewModel.fetchCollectors()
        advanceUntilIdle()

        viewModel.search("xyz")

        assertTrue(viewModel.collectors.value.isEmpty())
    }
}
