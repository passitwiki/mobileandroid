package com.passitwiki.passit.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * An object which builds an api interface with the base url,
 * adds a gson factory to convert a json file to e.g. a list of objects,
 * attaches a client, finally building and creating a full-fledged object to use.
 * As it is built from an interface, you create an instance and get the function you need.
 */
object RetrofitClient {
    //TODO CHANGE THE BASE URL
    private const val BASE_URL = "http://192.168.0.227:8000/api/"


    var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val newRequest: Request = chain.request().newBuilder()
                .build()
            return chain.proceed(newRequest)
        }
    }).build()

    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(Api::class.java)
    }

}