package com.passitwiki.passit.tools

import android.content.Context
import android.widget.Toast
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A singleton object to make just one call to the api for user data,
 * as it is supposedly a database-heavy operation.
 */
interface UserGetter {

    //TODO make it work
    /**
     * The function that takes in a token, makes a retrofit call to the
     * /users/me endpoint and retrieves the data.
     * @param token a token String in obtained in the login screen
     * @param appContext a this to the context for an onFailure error Toast
     * @return currently logged in User object
     */
    fun getUserData(token: String?, appContext: Context): User {
        val bearerToken = "Bearer $token"
        var user : User? = null

        RetrofitClient.instance.getUserInfo(bearerToken)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(appContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    user = response.body()!!
                }
            })
        Toast.makeText(appContext, user!!.username, Toast.LENGTH_LONG).show()

        return user!!
    }
}