package com.concept.finder.network

import com.concept.finder.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL =
    "https://maps.googleapis.com/maps/api/place/"

object Http {

    private val logging = HttpLoggingInterceptor().apply { level = BASIC }
    private val httpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(logging)
        }
    }

    private val conversionFactory: MoshiConverterFactory by lazy {
        MoshiConverterFactory.create()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addConverterFactory(conversionFactory)
            client(httpClient.build())
        }.build()
    }
}