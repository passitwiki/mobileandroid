package com.passitwiki.passit.models

import java.util.*

/**
 * Endpoint dataclass.
 */
data class User( val id: Int, val username: String, val profile:Profile)

data class Profile(val memberships: ArrayList<Int>, val field_age_groups: ArrayList<FoS>)

// Exercise - what type should memberships and f_a_g be - ArrayLists or custom?
//data class Memberships(val members:List<Int>) : ArrayList<Int>()
//data class FieldAgeGroups(val fosArray:List<FoS>)

data class FoS(val id: Int, val field_of_study: Int, val students_start_year: Int)
