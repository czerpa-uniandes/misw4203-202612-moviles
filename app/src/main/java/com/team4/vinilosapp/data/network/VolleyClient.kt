package com.team4.vinilosapp.data.network

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

object VolleyClient {

    private var requestQueue: RequestQueue? = null

    fun getInstance(context: Context): RequestQueue {
        return requestQueue ?: synchronized(this) {
            val queue = Volley.newRequestQueue(context.applicationContext)
            requestQueue = queue
            queue
        }
    }
}