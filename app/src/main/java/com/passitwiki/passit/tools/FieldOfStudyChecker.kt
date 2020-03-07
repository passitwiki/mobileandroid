package com.passitwiki.passit.tools

import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.FieldOfStudy
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FieldOfStudyChecker {

    fun updateFieldOfStudy(idFoS: Int) {
        RetrofitClient.instance.getFieldOfStudy(globalToken!!)
            .enqueue(object : Callback<List<FieldOfStudy>> {
                override fun onFailure(call: Call<List<FieldOfStudy>>, t: Throwable) {
                    Toast.makeText(globalContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<FieldOfStudy>>,
                    response: Response<List<FieldOfStudy>>
                ) {

                    Log.d("MyTag", "onResponse: ${response.body()!!}")

                    val listFos = response.body()!!

                    for (i in listFos) {
                        if (i.id.equals(idFoS)) {
                            globalSharedPreferences!!.edit().putString("current_fos", i.name)
                                .apply()
                        } else {
                            Log.d("MyTag", "onResponse: ${i.id}")

                        }
                    }
                }

            })
//        Log.d("MyTagFF", sharedPreferences.getString("current_fos", "hey") )
    }

    fun getFieldOfStudy(sharedPreferences: SharedPreferences): String {
        val slug = sharedPreferences.getString("current_fos", "null")!!
        return slug
    }

}