package com.team4.vinilosapp.data.adapters

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
import com.team4.vinilosapp.data.network.VinilosApiService
import com.team4.vinilosapp.ui.models.AddPrize
import com.team4.vinilosapp.ui.models.AddPrizeArtist
import com.team4.vinilosapp.ui.models.AddTrack
import com.team4.vinilosapp.ui.models.NewAlbum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VinilosServiceAdapterImpl(
    private val api: VinilosApiService
) : VinilosServiceAdapter {

    private var albumsCache: List<Album>? = null
    private val albumDetailCache = mutableMapOf<Int, Album>()

    private var musiciansCache: List<Performer>? = null
    private val artistDetailCache = mutableMapOf<Int, ArtistDetail>()

    private var bandsCache: List<Performer>? = null
    private val bandDetailCache = mutableMapOf<Int, BandDetail>()

    private var collectorsCache: List<Collector>? = null
    private val collectorDetailCache = mutableMapOf<Int, CollectorDetail>()
    private var prizesCache: List<Prize>? = null

    override suspend fun getAlbums(): List<Album> = withContext(Dispatchers.IO) {
        albumsCache ?: api.getAlbums().also { albumsCache = it }
    }

    override suspend fun getAlbumDetail(albumId: Int): Album = withContext(Dispatchers.IO) {
        albumDetailCache[albumId] ?: api.getAlbumDetail(albumId).also {
            albumDetailCache[albumId] = it
        }
    }

    override suspend fun createAlbum(album: NewAlbum) = withContext(Dispatchers.IO) {
        val response = api.createAlbum(album)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        albumsCache = null
    }

    override suspend fun addTrack(albumId: Int, track: AddTrack) = withContext(Dispatchers.IO) {
        val response = api.addTrack(albumId, track)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        albumsCache = null
        albumDetailCache.remove(albumId)

        Unit
    }

    override suspend fun getMusicians(): List<Performer> = withContext(Dispatchers.IO) {
        musiciansCache ?: api.getMusicians().also { musiciansCache = it }
    }

    override suspend fun getArtistDetail(artistId: Int): ArtistDetail = withContext(Dispatchers.IO) {
        artistDetailCache[artistId] ?: api.getArtistDetail(artistId).also {
            artistDetailCache[artistId] = it
        }
    }

    override suspend fun getBands(): List<Performer> = withContext(Dispatchers.IO) {
        bandsCache ?: api.getBands().also { bandsCache = it }
    }

    override suspend fun getBandDetail(bandId: Int): BandDetail = withContext(Dispatchers.IO) {
        bandDetailCache[bandId] ?: api.getBandDetail(bandId).also {
            bandDetailCache[bandId] = it
        }
    }

    override suspend fun addMusicianToBand(bandId: Int, musicianId: Int) = withContext(Dispatchers.IO) {
        val response = api.addMusicianToBand(bandId, musicianId)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        bandsCache = null
        musiciansCache = null
        bandDetailCache.remove(bandId)

        Unit
    }

    override suspend fun addAlbumToMusician(musicianId: Int, albumId: Int) = withContext(Dispatchers.IO) {
        val response = api.addAlbumToMusician(musicianId, albumId)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        artistDetailCache.remove(musicianId)
        albumsCache = null

        Unit
    }

    override suspend fun getCollectors(): List<Collector> = withContext(Dispatchers.IO) {
        collectorsCache ?: api.getCollectors().also { collectorsCache = it }
    }

    override suspend fun getCollectorDetail(collectorId: Int): CollectorDetail = withContext(Dispatchers.IO) {
        collectorDetailCache[collectorId] ?: api.getCollectorDetail(collectorId).also {
            collectorDetailCache[collectorId] = it
        }
    }

    override suspend fun addComment(
        albumId: String,
        comment: AlbumCommentRequest
    ): AlbumCommentResponse = withContext(Dispatchers.IO) {
        api.addComment(albumId, comment).also {
            albumDetailCache.remove(albumId.toIntOrNull())
        }
    }

    override suspend fun addAlbumToCollector(albumId: String, collectorId: String, albumToCollector: AddAlbumToCollectorRequest): AddAlbumToCollectorResponse = withContext(Dispatchers.IO) {
        api.addAlbumToCollector(collectorId, albumId, albumToCollector).also {
            collectorsCache = null
            collectorDetailCache.remove(collectorId.toIntOrNull())
        }
    }

    override suspend fun addPrize(prize: AddPrize) = withContext(Dispatchers.IO) {
        val response = api.addPrize(prize)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        prizesCache = null
        artistDetailCache.clear()
        bandDetailCache.clear()
    }

    override suspend fun getPrizes(): List<Prize> = withContext(Dispatchers.IO) {
        prizesCache ?: api.getPrizes().also { prizesCache = it }
    }

    override suspend fun associatePrizeArtist(
        prizeId: Int,
        artistId: Int,
        premiationDate: AddPrizeArtist
    ) = withContext(Dispatchers.IO) {
        val response = api.associtatePrizeArtist(prizeId, artistId, premiationDate)

        if (!response.isSuccessful) {
            throw Exception("Error ${response.code()}")
        }

        artistDetailCache.remove(artistId)

        Unit
    }
}