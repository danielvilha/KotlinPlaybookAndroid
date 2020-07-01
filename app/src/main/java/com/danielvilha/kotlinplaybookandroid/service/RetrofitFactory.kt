package com.danielvilha.kotlinplaybookandroid.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by danielvilha on 2019-07-24
 */
object RetrofitFactory {
    private const val BASE_URL = "http://jsonplaceholder.typicode.com/"

    private val client = OkHttpClient
        .Builder()
        .build()

    fun retrofit() : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}