package com.passitwiki.passit.repository

import com.passitwiki.passit.model.*
import com.passitwiki.passit.networking.Api
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.networking.ResponseHandler
import okhttp3.MultipartBody

class Repository(private val api: Api, private val responseHandler: ResponseHandler) {

    suspend fun handlePostUserLogin(
        username: String,
        password: String
    ): Resource<JwtCreateResponse> {
        return try {
            val response = api.postUserLogin(username, password)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handlePostSetPassword(
        bearerToken: String,
        newPassword: String,
        currentPassword: String
    ): Resource<Unit> {
        return try {
            val response = api.postSetPassword(bearerToken, newPassword, currentPassword)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleGetUserInfo(bearerToken: String): Resource<User> {
        return try {
            val response = api.getUserInfo(bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun handleGetFieldOfStudy(bearerToken: String): Resource<List<FieldOfStudy>> {
        return try {
            val response = api.getFieldOfStudy(bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }


    suspend fun handleGetNews(bearerToken: String): Resource<List<News>> {
        return try {
            val response = api.getNews(bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleDeleteNews(id: Int, bearerToken: String): Resource<Unit> {
        return try {
            val response = api.deleteNews(id, bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handlePatchNews(
        bearerToken: String,
        id: Int,
        title: String,
        content: String
    ): Resource<News> {
        return try {
            val response = api.patchNews(id, bearerToken, title, content)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleGetEvents(bearerToken: String): Resource<List<Event>> {
        return try {
            val response = api.getEvents(bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handlePostNews(
        bearerToken: String,
        title: MultipartBody.Part,
        content: MultipartBody.Part,
        subjectGroup: Int,
        fieldAgeGroup: Int,
        file: MultipartBody.Part?
    ): Resource<Unit> {
        return try {
            val response =
                api.postNews(bearerToken, title, content, subjectGroup, fieldAgeGroup, file)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handlePostRefresh(refreshToken: String): Resource<RefreshResponse> {
        return try {
            val response = api.postRefresh(refreshToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleGetSubjects(semester: Int, fieldOfStudy: Int): Resource<List<Subject>> {
        return try {
            val response = api.getSubjects(semester, fieldOfStudy)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleGetLecturers(): Resource<List<Lecturer>> {
        return try {
            val response = api.getLecturers()
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }

    suspend fun handleGetSubjectsAgeGroup(bearerToken: String): Resource<List<SubjectGroup>> {
        return try {
            val response = api.getSubjectsAgeGroup(bearerToken)
            responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}