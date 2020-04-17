package com.passitwiki.passit.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.passitwiki.passit.R
import com.passitwiki.passit.repository.Repository
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject


/**
 * An activity class that has 2 editTexts and a Button to login.
 * If both fields have been input,
 * there is a Retrofit call that sends a POST to /jwt/create,
 * which returns the account info in JwtCreateResponse.
 * The response is later displayed.
 */
class LoginActivity : AppCompatActivity() {
    private val repository: Repository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin.setOnClickListener {
            val email = editTextIndex.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty()) {
                editTextIndex.error = "Email required"
                editTextIndex.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }
            runBlocking {
                loginAndGoToMain(email, password)
            }
        }
    }

    private suspend fun loginAndGoToMain(email: String, password: String) {
        val response = repository.handlePostUserLogin(email, password).data
        Log.d(
            "MyTagExplicitNetworking",
            "LoginActivity: loginAndGoToMain response $response"
        )

        if (response?.access != null) {
            val accessToken = "Bearer ${response.access}"
            val refreshToken = response.refresh
            openUpMain(accessToken, refreshToken)
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.failed_login),
                Toast.LENGTH_LONG
            ).show()
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