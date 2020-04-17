package com.passitwiki.passit.utilities

import android.annotation.SuppressLint
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.activity.sharedPreferences
import com.passitwiki.passit.model.FieldAgeGroup
import com.passitwiki.passit.model.User
import com.passitwiki.passit.repository.Repository

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

class Utilities(private val repository: Repository) {

    /**
     * Token refresher.
     */
    @SuppressLint("ApplySharedPref")
    suspend fun justRefresh(key: String): String {
        val refreshToken = sharedPreferences.getString("refresh", "error")!!
        Log.d(
            "MyTagExplicit",
            "$key TokenRefresher: justRefresh init refresh: $refreshToken"
        )
        val response = repository.handlePostRefresh(refreshToken).data!!

        Log.d(
            "MyTagExplicit",
            "$key TokenRefresher: got response: ${response.access}"
        )
        accessToken = "Bearer ${response.access}"
        Log.d(
            "MyTagExplicit",
            "$key TokenRefresher: set accessToken: $accessToken"
        )
        sharedPreferences.edit().putString(
            "access",
            accessToken
        ).commit()

        return accessToken
    }

    /**
     * Gets a user response and by a function sequence - sets locally a full user name,
     * a fieldAgeGroupID, a field of study name and ID number.
     */
    suspend fun setUserAndFos() {
        val response = repository.handleGetUserInfo(accessToken).data

        if (response == null) {
            accessToken = justRefresh("userSetter")
            setUserAndFos()
        } else {
            setUserFullNameForFutureSettingsUse(response)
            val fieldAgeGroupId = localFieldAgeGroupIdSetter(response)
            val chosenFieldAgeGroup: FieldAgeGroup = chooseFieldGroup(response, fieldAgeGroupId)
            Log.d(
                "MyTagExplicit",
                "8*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: ID of the fag is: $fieldAgeGroupId"
            )
            Log.d(
                "MyTagExplicit",
                "9*. MainActivity: onCreate: UserSetter.setUserAndFos: onResponse: The chosen field age group: $chosenFieldAgeGroup"
            )

            localFieldOfStudySetter(chosenFieldAgeGroup)
        }
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
    private fun chooseFieldGroup(user: User, fieldAgeGroupId: Int): FieldAgeGroup {
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
    @SuppressLint("ApplySharedPref")
    suspend fun localFieldOfStudySetter(
        chosenFieldAgeGroup: FieldAgeGroup
    ) {
        val response = repository.handleGetFieldOfStudy(accessToken).data

        if (response == null) {
            accessToken = justRefresh("userSetter onNull")
            setUserAndFos()
        } else {
            response.forEach {
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
    }


}