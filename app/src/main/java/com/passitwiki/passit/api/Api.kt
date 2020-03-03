package com.passitwiki.passit.api

import com.passitwiki.passit.models.*
import retrofit2.Call
import retrofit2.http.*

/**
 * A simple api interface which specifies endpoints and functions.
 * Used and instance built in RetrofitClient.
 */
interface Api {

    @FormUrlEncoded
    @POST("auth/jwt/create/")
    fun postUserLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<JwtCreateResponse>

    @GET("auth/users/me/?expand=profile,profile.field_age_groups")
    fun getUserInfo(
        @Header("Authorization") bearerToken: String
    ): Call<User>

    @GET("news/")
    fun getNews(
        @Header("Authorization") bearerToken: String
    ): Call<List<News>>

    @GET("subjects/")
    fun getSubjects(): Call<List<Subject>>

    @GET("lecturers/")
    fun getLecturers(): Call<List<Lecturer>>

}
