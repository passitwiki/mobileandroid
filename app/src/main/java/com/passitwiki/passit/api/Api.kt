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

    @FormUrlEncoded
    @POST("auth/users/set_password/")
    fun postSetPassword(
        @Header("Authorization") bearerToken: String,
        @Field("new_password") username: String,
        @Field("current_password") password: String
    ): Call<Unit>

    @GET("auth/users/me/?expand=profile,profile.field_age_groups,profile.memberships")
    fun getUserInfo(
        @Header("Authorization") bearerToken: String
    ): Call<User>

    @GET("fieldsofstudy/")
    fun getFieldOfStudy(
        @Header("Authorization") bearerToken: String
    ): Call<List<FieldOfStudy>>

    @GET("news/")
    fun getNews(
        @Header("Authorization") bearerToken: String
    ): Call<List<News>>

    @GET("events/")
    fun getEvents(
        @Header("Authorization") bearerToken: String
    ): Call<List<Event>>

    @FormUrlEncoded
    @POST("news/")
    fun postNews(
        @Header("Authorization") bearerToken: String,
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("subject_group") subjectGroup: Int,
        @Field("field_age_group") fieldAgeGroup: Int
    ): Call<Unit>

//    @GET("subjects/")
//    fun getSubjects(): Call<List<Subject>>

    @GET("subjects/")
    fun getSubjects(
        @Query("semester") semester: Int,
        @Query("field_of_study") fieldOfStudy: Int
    ): Call<List<Subject>>



    @GET("lecturers/")
    fun getLecturers(): Call<List<Lecturer>>

}
