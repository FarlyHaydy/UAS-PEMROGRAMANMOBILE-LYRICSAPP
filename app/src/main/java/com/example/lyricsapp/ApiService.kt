package com.example.lyricsapp

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v1/{artist}/{title}")
    fun searchLyrics(@Path("artist") artist: String, @Path("title") title: String): Call<LyricsResponse>

    companion object {
        private const val BASE_URL = "https://api.lyrics.ovh/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

class ApiCallback<T>(private val tag: String, private val onResponse: (Response<T>) -> Unit) : Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.d(tag, "onResponse: ${response.body()}")
        onResponse(response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        Log.e(tag, "onFailure", t)
    }
}
