package com.team4.vinilosapp.data.network

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.team4.vinilosapp.data.models.Album
import com.team4.vinilosapp.data.models.Comment
import com.team4.vinilosapp.data.models.Performer
import com.team4.vinilosapp.data.models.Track
import org.json.JSONArray
import org.json.JSONObject
import com.android.volley.Request
import com.team4.vinilosapp.ui.models.NewAlbum

class AlbumService {
    fun getAlbums(
        context: Context,
        onSuccess: (List<Album>) -> Unit,
        onError: (String) -> Unit
    ) {

        val url = "http://10.0.2.2:3000/albums"

        val request = JsonArrayRequest(
            url,
            { response: JSONArray ->

                if (response.length() == 0) {
                    onSuccess(emptyList())
                    return@JsonArrayRequest
                }

                val albums = mutableListOf<Album>()

                for (i in 0 until response.length()) {

                    val obj = response.getJSONObject(i) ?: continue

                    val tracksArray = obj.optJSONArray("tracks") ?: JSONArray()
                    val tracks = mutableListOf<Track>()

                    for (j in 0 until tracksArray.length()) {
                        val t = tracksArray.getJSONObject(j) ?: continue

                        tracks.add(
                            Track(
                                id = t.getInt("id"),
                                name = t.getString("name"),
                                duration = t.getString("duration")
                            )
                        )
                    }

                    val commentsArray = obj.optJSONArray("comments") ?: JSONArray()
                    val comments = mutableListOf<Comment>()

                    for (j in 0 until commentsArray.length()) {
                        val c = commentsArray.getJSONObject(j) ?: continue

                        comments.add(
                            Comment(
                                id = c.getInt("id"),
                                description = c.getString("description"),
                                rating = c.getInt("rating")
                            )
                        )
                    }

                    val performersArray = obj.optJSONArray("performers") ?: JSONArray()
                    val performers = mutableListOf<Performer>()

                    for (j in 0 until performersArray.length()) {
                        val p = performersArray.getJSONObject(j) ?: continue
                        val birthDate = p.optString("birthDate")
                            .ifEmpty { p.optString("creationDate") }

                        performers.add(
                            Performer(
                                id = p.getInt("id"),
                                name = p.getString("name"),
                                image = p.getString("image"),
                                description = p.getString("description"),
                                birthDate = birthDate
                            )
                        )
                    }

                    val album = Album(
                        id = obj.getString("id"),
                        name = obj.getString("name"),
                        cover = obj.getString("cover"),
                        releaseDate = obj.getString("releaseDate"),
                        description = obj.getString("description"),
                        genre = obj.getString("genre"),
                        recordLabel = obj.getString("recordLabel"),
                        tracks = tracks,
                        performers = performers,
                        comments = comments
                    )

                    albums.add(album)
                }

                onSuccess(albums)
            },
            { error ->
                val statusCode = error.networkResponse?.statusCode
                val errorBody = error.networkResponse?.data?.let { String(it) }
                Log.e("MY MESSAGE", statusCode.toString())
                Log.e("MY MESSAGE", errorBody.toString())
                Log.e("MY MESSAGE", error.message.toString())
                onError(error.message ?: "Error desconocido")
            }
        )

        VolleyClient.getInstance(context).add(request)
    }

    fun createAlbum(
        context: Context,
        album: NewAlbum,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val url = "http://10.0.2.2:3000/albums"

        val body = JSONObject().apply {
            put("name", album.title)
            put("genre", album.genre)
            put("cover", album.cover)
            put("description", album.description)
            put("releaseDate", album.releaseDate)
            put("recordLabel", album.recordLabel)
        }

        Log.d("MY MESSAGE", body.toString())

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            body,
            {
                onSuccess()
            },
            { error ->
                onError(error.message ?: "Error")
            }
        )

        VolleyClient.getInstance(context).add(request)
    }
}