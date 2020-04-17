package com.passitwiki.passit.networking

import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * The url to the API without any endpoint additions.
 */
const val BASE_URL = "https://passit.beyondthe.dev/api/"

/**
 * @return Retrofit object with the right url, a converter and a client.
 */
fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
}

/**
 * @return an OkHttpClient for building a Retrofit object
 */
fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        .addInterceptor(HttpLoggingInterceptor()).build()
}

/**
 * @return a created Api object
 */
fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)