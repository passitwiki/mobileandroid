package com.passitwiki.passit.tools

import android.annotation.SuppressLint
import android.util.Log
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.activities.sharedPreferences
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.models.FieldAgeGroup
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

    /**
     * Gets a user response and by a function sequence - sets locally a full user name,
     * a fieldAgeGroupID, a field of study name and ID number.
     * @param access authorization
     */
    fun setUserAndFos(access: String) {
        RetrofitClient.instance.getUserInfo(access).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MyTag", "Error: $t")
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() == null) {
                    justRefresh("userSetter")
                    setUserAndFos(accessToken)
                }
                //Making the user object
                val user = User(
                    response.body()!!.id,
                    response.body()!!.username,
                    response.body()!!.profile,
                    response.body()!!.first_name,
                    response.body()!!.last_name
                )

                setUserFullNameForFutureSettingsUse(user)

                val fieldAgeGroupId = localFieldAgeGroupIdSetter(user)
                val chosenFieldAgeGroup: FieldAgeGroup = chooseFieldGroup(user, fieldAgeGroupId)
                Log.d(
                    "MyTagExplicit",
                    "8*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: ID of the fag is: $fieldAgeGroupId"
                )
                Log.d(
                    "MyTagExplicit",
                    "9*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: The chosen field age group: $chosenFieldAgeGroup"
                )

                localFieldOfStudySetter(access, chosenFieldAgeGroup)

            }
        })
    }

    /**
     * Stores locally the full name and surname of a user for future display.
     * @param user the user got in the request reply
     */
    @SuppressLint("ApplySharedPref")
    //TODO remove suppression
    fun setUserFullNameForFutureSettingsUse(user: User) {
        val userFullName = "${user.first_name} ${user.last_name}"
        Log.d(
            "MyTagExplicit",
            "6*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: set current_user_full_name: $userFullName"
        )
        sharedPreferences.edit().putString("current_user_full_name", userFullName).commit()
    }

    /**
     * Goes through the user memberships to see if there is a default fieldAgeGroup,
     * if so - stores locally the of such, else - takes the first fAG there is.
     * @return an integer that is the current fieldOfAgeGroupId
     */
    @SuppressLint("ApplySharedPref")
    fun localFieldAgeGroupIdSetter(user: User): Int {
        var fieldOfAgeGroupId = 0
        var isDefaultCounter = 0
        for (member in user.profile.memberships) {
            if (member.is_default) {
                isDefaultCounter = 1
                fieldOfAgeGroupId = member.field_age_group
                Log.d(
                    "MyTagExplicit",
                    "7*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: fagSetterId: set current_fag: $fieldOfAgeGroupId"
                )
                sharedPreferences.edit().putInt(
                    "current_fag",
                    fieldOfAgeGroupId
                ).commit()
            }
        }
        if (isDefaultCounter == 0) {
            fieldOfAgeGroupId = user.profile.memberships[0].field_age_group
            Log.d(
                "MyTagExplicit",
                "7*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: fagSetterId(no default): set current_fag: $fieldOfAgeGroupId"
            )
            sharedPreferences.edit().putInt(
                "current_fag",
                fieldOfAgeGroupId
            ).commit()
        }
        return fieldOfAgeGroupId
    }

    /**
     * Goes through user's fieldAgeGroups
     * @param fieldAgeGroupId the decisive id of the fieldAgeGroup
     * @return a fieldAgeGroup that is his default or chosen based on the above
     */
    fun chooseFieldGroup(user: User, fieldAgeGroupId: Int): FieldAgeGroup {
        var chosen = FieldAgeGroup(0, 0, 0)
        user.profile.field_age_groups.forEach {
            if (it.id == fieldAgeGroupId) {
                chosen = it
            }
        }
        return chosen
    }

    /**
     * Requests fieldOfStudies, iterates through them and if a fieldOfAgeGroup's fieldOfStudy id
     * is the same as id of a gotten fieldOfStudy - it locally saves the name and start year
     * of the fieldOfStudy, e.g. "Teleinformatyka 2018", as well as the id of the fieldOfStudy.
     */
    fun localFieldOfStudySetter(
        accessToken: String,
        chosenFieldAgeGroup: FieldAgeGroup
    ) {
        RetrofitClient.instance.getFieldOfStudy(accessToken)
            .enqueue(object : Callback<List<FieldOfStudy>> {
                override fun onFailure(call: Call<List<FieldOfStudy>>, t: Throwable) {
                    Log.d("MyTag", "Error: $t")
                }

                @SuppressLint("ApplySharedPref")
                //TODO remove suppression
                override fun onResponse(
                    call: Call<List<FieldOfStudy>>,
                    response: Response<List<FieldOfStudy>>
                ) {
                    if (response.body() == null) {
                        justRefresh("userSetter onNull")
                        setUserAndFos(accessToken)
                    }
                    response.body()?.forEach {
                        if (it.id == chosenFieldAgeGroup.field_of_study) {
                            val fieldOfStudyName =
                                "${it.name} ${chosenFieldAgeGroup.students_start_year}"
                            val fieldOfStudyInt = chosenFieldAgeGroup.field_of_study

                            Log.d(
                                "MyTagExplicit",
                                "10*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: fosSetter: current_fos: $fieldOfStudyName"
                            )
                            sharedPreferences.edit().putString(
                                "current_fos",
                                fieldOfStudyName
                            ).commit()

                            Log.d(
                                "MyTagExplicit",
                                "11*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: fosSetter: current_fos_int: $fieldOfStudyInt"
                            )
                            sharedPreferences.edit().putInt(
                                "current_fos_int",
                                fieldOfStudyInt
                            ).commit()
                        }
                    }
                }
            })
    }

}