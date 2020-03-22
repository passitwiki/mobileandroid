package com.passitwiki.passit.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.JwtCreateResponse
import com.passitwiki.passit.tools.refreshAndGetAccess
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

lateinit var sharedPreferences: SharedPreferences
var accessToken: String = ""
var refreshToken: String = ""
//lateinit var accessToken:String
//lateinit var refreshToken:String


/**
 * An activity class that has 2 editTexts and a Button to login.
 * If both fields have been input,
 * there is a Retrofit call that sends a POST to /jwt/create,
 * which returns the account info in JwtCreateResponse.
 * The response is later displayed.
 */
class LoginActivity : AppCompatActivity(), CoroutineScope {

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        val loggedIn = sharedPreferences.getBoolean("logged_in", false)
        Log.d("MyTagExplicit", "1. LoginActivity: onCreate: get logged_in: $loggedIn")

        val refresh = sharedPreferences.getString("refresh", "")!!
        Log.d("MyTagExplicit", "2. LoginActivity: onCreate: get refresh: $refresh")

        if (loggedIn) {
            refreshAndGetAccess(this, refresh)
        } else {
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

                loginAndGoToMain(email, password)

            }
        }
    }


    fun loginAndGoToMain(email: String, password: String) {
        RetrofitClient.instance.postUserLogin(email, password)
            .enqueue(object : Callback<JwtCreateResponse> {
                override fun onFailure(call: Call<JwtCreateResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<JwtCreateResponse>,
                    response: Response<JwtCreateResponse>
                ) {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "LoginActivity: loginAndGoToMain response ${response.body()}"
                    )

                    if (response.body()?.access != null) {
                        accessToken = "Bearer ${response.body()?.access!!}"
                        refreshToken = response.body()?.refresh!!
                        startTheMainActivity(accessToken, refreshToken)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.failed_login),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }


    @SuppressLint("ApplySharedPref")
    //TODO remove suppression
    fun startTheMainActivity(access: String, refresh: String) {
        val intent = (Intent(applicationContext, MainActivity::class.java))
        intent.putExtra("access", access)
        intent.putExtra("refresh", refresh)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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
        finish()
    }

}