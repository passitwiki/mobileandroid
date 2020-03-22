package com.passitwiki.passit.tools

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.passitwiki.passit.activities.LoginActivity
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.activities.refreshToken
import com.passitwiki.passit.activities.sharedPreferences
import com.passitwiki.passit.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//TODO LOGIC CHECK

fun refreshAndGetAccess(activity: LoginActivity, refresh: String) {

    Log.d(
        "MyTagExplicit",
        "TokenRefresher: onNullResponse: ref token: $refresh"
    )
    RetrofitClient.instance.postRefresh(refresh)
        .enqueue(object : Callback<RefreshResponse> {
            override fun onFailure(call: Call<RefreshResponse>, t: Throwable) {
                Log.d("MyTag", "onFailure: $t")
            }

            //TODO remove suppression
            @SuppressLint("ApplySharedPref")
            override fun onResponse(
                call: Call<RefreshResponse>,
                response: Response<RefreshResponse>
            ) {
                Log.d(
                    "MyTagExplicit",
                    "TokenRefresher: onNullResponse: ref token: $refresh"
                )
                if (response.body() == null) {
                    Log.d(
                        "MyTagExplicit",
                        "TokenRefresher: onNullResponse: set logged_in to false"
                    )
                    sharedPreferences.edit().putBoolean("logged_in", false).apply()
                    val intent =
                        (Intent(activity.applicationContext, LoginActivity::class.java))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(activity.applicationContext, intent, null)
                    activity.finish()
                } else {
                    accessToken = "Bearer ${response.body()!!.access}"
                    Log.d(
                        "MyTagExplicit",
                        "3*. LoginActivity: onCreate: TokenRefresher: set accessToken: $accessToken"
                    )
                    sharedPreferences.edit().putString(
                        "access",
                        accessToken
                    ).commit()
                    activity.startTheMainActivity(accessToken, refresh)
                }
            }
        })
}

fun justRefresh(key: String) {

    refreshToken = sharedPreferences.getString("refresh", refreshToken)!!
    Log.d(
        "MyTagExplicit",
        "$key TokenRefresher: justRefresh init refresh: $refreshToken"
    )
    RetrofitClient.instance.postRefresh(refreshToken)
        .enqueue(object : Callback<RefreshResponse> {
            override fun onFailure(call: Call<RefreshResponse>, t: Throwable) {
                Log.d("MyTag", "onFailure: $t")
            }

            //TODO remove suppression
            @SuppressLint("ApplySharedPref")
            override fun onResponse(
                call: Call<RefreshResponse>,
                response: Response<RefreshResponse>
            ) {
                Log.d(
                    "MyTagExplicit",
                    "$key TokenRefresher: got response: ${response.body()}"
                )
                accessToken = "Bearer ${response.body()!!.access}"
                Log.d(
                    "MyTagExplicit",
                    "$key TokenRefresher: set accessToken: $accessToken"
                )
                sharedPreferences.edit().putString(
                    "access",
                    accessToken
                ).commit()
            }
        })
}

data class RefreshResponse(val access: String)