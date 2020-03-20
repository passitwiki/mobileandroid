package com.passitwiki.passit.tools

import android.content.SharedPreferences
import android.util.Log
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.FieldOfStudy
import com.passitwiki.passit.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A singleton object to make just one call to the api for user data,
 * as it is supposedly a database-heavy operation.
 */
object UserSetter {

    fun setUserAndFos(accessToken: String, sharedPref: SharedPreferences) {
        RetrofitClient.instance.getUserInfo(accessToken)
            .enqueue(object : Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.d("MyTag", "Error: $t")
                }

                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    val user = User(
                        response.body()!!.id,
                        response.body()!!.username,
                        response.body()!!.profile,
                        response.body()!!.first_name,
                        response.body()!!.last_name
                    )
                    sharedPref.edit().putString(
                        "current_user_full_name",
                        user.first_name + " " + user.last_name
                    ).apply()

                    for (member in user.profile.memberships) {
                        var hit = 0
                        if (member.is_default) {
                            hit = 1
                            sharedPref.edit().putString(
                                "current_fag",
                                member.field_age_group.toString()
                            ).apply()
                        }
                        if (hit == 0) {
                            sharedPref.edit().putString(
                                "current_fag",
                                user.profile.memberships[0].field_age_group.toString()
                            ).apply()
                        }
                    }



                    RetrofitClient.instance.getFieldOfStudy(accessToken)
                        .enqueue(object : Callback<List<FieldOfStudy>> {
                            override fun onFailure(call: Call<List<FieldOfStudy>>, t: Throwable) {
                                Log.d("MyTag", "Error: $t")
                            }

                            override fun onResponse(
                                call: Call<List<FieldOfStudy>>,
                                response: Response<List<FieldOfStudy>>
                            ) {
                                response.body()!!.forEach {
                                    if (it.id == user.profile.field_age_groups[0].field_of_study) {
                                        sharedPref.edit().putString(
                                            "current_fos",
                                            it.name + " " + user.profile.field_age_groups[0].students_start_year
                                        ).apply()
                                        sharedPref.edit().putInt(
                                            "current_fos_int",
                                            user.profile.field_age_groups[0].field_of_study
                                        ).apply()
                                    }
                                }
                            }
                        })

                }
            })
    }
}