package com.passitwiki.passit.tools

import android.content.SharedPreferences
import android.util.Log
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.FieldOfStudy
import com.passitwiki.passit.models.FoS
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
                    ).commit()

                    var fag = 0
                    var fagObject: FoS = FoS(0, 0, 0)

                    for (member in user.profile.memberships) {
                        var hit = 0
                        if (member.is_default) {
                            hit = 1
                            fag = member.field_age_group
                            sharedPref.edit().putInt(
                                "current_fag",
                                member.field_age_group
                            ).commit()
                        }
                        if (hit == 0) {
                            fag = user.profile.memberships[0].field_age_group
                            sharedPref.edit().putInt(
                                "current_fag",
                                user.profile.memberships[0].field_age_group
                            ).commit()
                        }
                    }
                    user.profile.field_age_groups.forEach {
                        if (it.id == fag) {
                            fagObject = it
                        }
                    }
                    Log.d("MyTag", "Setting fag object!! ${fagObject.field_of_study}")


                    RetrofitClient.instance.getFieldOfStudy(accessToken)
                        .enqueue(object : Callback<List<FieldOfStudy>> {
                            override fun onFailure(call: Call<List<FieldOfStudy>>, t: Throwable) {
                                Log.d("MyTag", "Error: $t")
                            }

                            override fun onResponse(
                                call: Call<List<FieldOfStudy>>,
                                response: Response<List<FieldOfStudy>>
                            ) {
                                Log.d("MyTag", response.body().toString())
                                response.body()?.forEach {
                                    if (it.id == fagObject.field_of_study) {
                                        sharedPref.edit().putString(
                                            "current_fos",
                                            it.name + " " + fagObject.students_start_year
                                        ).commit()
                                        sharedPref.edit().putInt(
                                            "current_fos_int",
                                            fagObject.field_of_study
                                        ).commit()
                                        Log.d(
                                            "MyTag",
                                            "Setting fag object!! ${sharedPref.getInt(
                                                "current_fos_int",
                                                0
                                            )}"
                                        )

                                    }
                                }
                            }
                        })

                }
            })
    }
}