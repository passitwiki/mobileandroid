package com.passitwiki.passit.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.passitwiki.passit.R
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.JwtCreateResponse
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * An activity class that has 2 editTexts and a Button to login.
 * If both fields have been input,
 * there is a Retrofit call that sends a POST to /jwt/create,
 * which returns the account info in JwtCreateResponse.
 * The response is later displayed.
 */
class LoginActivity : AppCompatActivity() {

    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
        val logged_in =
            sharedPref.getBoolean("logged_in", false)

        if (logged_in) {
            val email = sharedPref.getString("username", "null")
            val password = sharedPref.getString("password", "null")
            clickToMain(email!!, password!!)
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

                clickToMain(email, password)

            }
        }
    }

    fun clickToMain(email: String, password: String) {
        RetrofitClient.instance.postUserLogin(email, password)
            .enqueue(object : Callback<JwtCreateResponse> {
                override fun onFailure(call: Call<JwtCreateResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<JwtCreateResponse>,
                    response: Response<JwtCreateResponse>
                ) {
                    if (response.body()?.access != null) {
                        val intent = (Intent(applicationContext, MainActivity::class.java))
                        intent.putExtra("token", response.body()?.access)
                        intent.putExtra("refresh", response.body()?.refresh)
                        startActivity(intent)
                        sharedPref.edit().putString(
                            "username",
                            email
                        ).commit()
                        sharedPref.edit().putString(
                            "password",
                            password
                        ).commit()
                        sharedPref.edit().putBoolean(
                            "logged_in",
                            true
                        ).commit()
                        finish()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No active account found with the given credentials",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}