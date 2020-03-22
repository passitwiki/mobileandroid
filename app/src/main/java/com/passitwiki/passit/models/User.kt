package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class User(
    val id: Int,
    val username: String,
    val profile: Profile,
    val first_name: String,
    val last_name: String
)

data class Profile(
    val memberships: List<Memberships>,
    val field_age_groups: List<FieldAgeGroup>
)
//data class Profile(
//    val memberships: ArrayList<Memberships>,
//    val field_age_groups: ArrayList<FieldAgeGroup>
//)

// Exercise - what type should memberships and f_a_g be - ArrayLists or custom?
//data class Memberships(val members:List<Int>) : ArrayList<Int>()
//data class FieldAgeGroups(val fosArray:List<FoS>)

data class Memberships(
    val id: Int,
    val profile: Int,
    val field_age_group: Int,
    val type: Int,
    val is_default: Boolean
)

data class FieldAgeGroup(val id: Int, val field_of_study: Int, val students_start_year: Int)