package com.passitwiki.passit.networking

import com.passitwiki.passit.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * A simple api interface which specifies endpoints and functions.
 * Used and instance built in RetrofitClient.
 */
interface Api {

    @FormUrlEncoded
    @POST("auth/jwt/create/")
    suspend fun postUserLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): JwtCreateResponse

    @FormUrlEncoded
    @POST("auth/users/set_password/")
    suspend fun postSetPassword(
        @Header("Authorization") bearerToken: String,
        @Field("new_password") newPassword: String,
        @Field("current_password") currentPassword: String
    ): Response<Unit>

    @GET("auth/users/me/?expand=profile,profile.field_age_groups,profile.memberships")
    suspend fun getUserInfo(
        @Header("Authorization") bearerToken: String
    ): User

    @GET("fieldsofstudy/")
    suspend fun getFieldOfStudy(
        @Header("Authorization") bearerToken: String
    ): List<FieldOfStudy>

    @GET("news/")
    suspend fun getNews(
        @Header("Authorization") bearerToken: String
    ): List<News>

    @DELETE("news/{id}/")
    suspend fun deleteNews(
        @Path("id") id: Int,
        @Header("Authorization") bearerToken: String
    )

    @FormUrlEncoded
    @PATCH("news/{id}/")
    suspend fun patchNews(
        @Path("id") id: Int,
        @Header("Authorization") bearerToken: String,
        @Field("title") title: String,
        @Field("content") content: String
    ): News

    @GET("events/")
    suspend fun getEvents(
        @Header("Authorization") bearerToken: String
    ): List<Event>

    //    @FormUrlEncoded
    @Multipart
    @POST("news/")
    suspend fun postNews(
        @Header("Authorization") bearerToken: String,
        @Part title: MultipartBody.Part,
        @Part content: MultipartBody.Part,
        @Part("subject_group") subjectGroup: Int,
        @Part("field_age_group") fieldAgeGroup: Int,
        @Part file: MultipartBody.Part?
    )

    @FormUrlEncoded
    @POST("auth/jwt/refresh/")
    suspend fun postRefresh(
        @Field("refresh") refreshToken: String
    ): RefreshResponse

    @GET("subjects/")
    suspend fun getSubjects(
        @Query("semester") semester: Int,
        @Query("field_of_study") fieldOfStudy: Int
    ): List<Subject>

    @GET("lecturers/")
    suspend fun getLecturers(): List<Lecturer>

    @GET("subjectsagegroup/?expand=subject_name")
    suspend fun getSubjectsAgeGroup(
        @Header("Authorization") bearerToken: String
    ): List<SubjectGroup>

}
