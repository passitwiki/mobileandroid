package com.passitwiki.passit.api

import com.passitwiki.passit.models.*
import com.passitwiki.passit.tools.RefreshResponse
import okhttp3.MultipartBody
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

    @DELETE("news/{id}/")
    fun deleteNews(
        @Path("id") id: Int,
        @Header("Authorization") bearerToken: String
    ): Call<Unit>

    @FormUrlEncoded
    @PATCH("news/{id}/")
    fun patchNews(
        @Path("id") id: Int,
        @Header("Authorization") bearerToken: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): Call<News>

    @GET("events/")
    fun getEvents(
        @Header("Authorization") bearerToken: String
    ): Call<List<Event>>

    //    @FormUrlEncoded
    @Multipart
    @POST("news/")
    fun postNews(
        @Header("Authorization") bearerToken: String,
        @Part("title") title: String,
        @Part("content") content: String,
        @Part("subject_group") subjectGroup: Int,
        @Part("field_age_group") fieldAgeGroup: Int,
        @Part file: MultipartBody.Part?
    ): Call<Unit>


    @FormUrlEncoded
    @POST("auth/jwt/refresh/")
    fun postRefresh(
        @Field("refresh") refreshToken: String
    ): Call<RefreshResponse>

    @GET("subjects/")
    fun getSubjects(
        @Query("semester") semester: Int,
        @Query("field_of_study") fieldOfStudy: Int
    ): Call<List<Subject>>

    @GET("lecturers/")
    fun getLecturers(): Call<List<Lecturer>>

}
