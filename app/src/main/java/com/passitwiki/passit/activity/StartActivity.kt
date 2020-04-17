package com.passitwiki.passit.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.passitwiki.passit.R
import com.passitwiki.passit.repository.Repository
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

lateinit var sharedPreferences: SharedPreferences

class StartActivity : AppCompatActivity() {
    private val repository: Repository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        progressStart.indeterminateDrawable = getDrawable(R.drawable.progress_gradient)

        val loggedIn = sharedPreferences.getBoolean("logged_in", false)
        Log.d("MyTagExplicit", "1. LoginActivity: onCreate: get logged_in: $loggedIn")

        val refresh = sharedPreferences.getString("refresh", "")!!
        Log.d("MyTagExplicit", "2. LoginActivity: onCreate: get refresh: $refresh")

        if (loggedIn) {
            runBlocking {
                loggedInGoToMainOrExpired(refresh)
            }
        } else {
            openUpLogin()
        }

    }

    private fun openUpLogin() {
        //open login
        val intent = (Intent(applicationContext, LoginActivity::class.java))
        startActivity(intent)
        //end this activity
        finish()
    }

    @SuppressLint("ApplySharedPref")
    suspend fun loggedInGoToMainOrExpired(refresh: String) {
        val response = repository.handlePostRefresh(refresh).data
        if (response == null) {
            //Token expired
            Log.d(
                "MyTagExplicit",
                "TokenRefresher: onNullResponse: set logged_in to false"
            )
            sharedPreferences.edit().putBoolean("logged_in", false).apply()
            val intent =
                (Intent(applicationContext, LoginActivity::class.java))
            startActivity(intent)
            finish()
        } else {
            //Valid token
            val accessToken = "Bearer ${response.access}"
            Log.d(
                "MyTagExplicit",
                "3*. LoginActivity: onCreate: TokenRefresher: set accessToken: $accessToken"
            )
            sharedPreferences.edit().putString(
                "access",
                accessToken
            ).commit()
            openUpMain(accessToken, refresh)
        }
    }


    @SuppressLint("ApplySharedPref")
    fun openUpMain(access: String, refresh: String) {
        //open up main
        val intent = (Intent(applicationContext, MainActivity::class.java))
        intent.putExtra("access", access)
        intent.putExtra("refresh", refresh)
        startActivity(intent)
        //log the progress
        Log.d("MyTagExplicit", "LoginActivity: startTheMainActivity: set logged_in to true")
        sharedPreferences.edit().putBoolean(
            "logged_in",
            true
        ).commit()
        Log.d("MyTagExplicit", "LoginActivity: startTheMainActivity: set refresh to $refresh")
        sharedPreferences.edit().putString(
            "refresh",
            refresh
        ).commit()
        Log.d("MyTagExplicit", "LoginActivity: startTheMainActivity: set access to $access")
        sharedPreferences.edit().putString(
            "access",
            access
        ).commit()
        //end this activity
        finish()
    }
}
