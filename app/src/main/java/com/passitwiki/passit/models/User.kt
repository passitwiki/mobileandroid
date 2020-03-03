package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class User( val id: Int, val username: String, val profile:Profile)

data class Profile(val memberships:Memberships, val field_age_groups:FieldAgeGroups)

class Memberships(val members:List<Int>){

}

data class FieldAgeGroups(val fosArray:List<FoS>)

data class FoS(val id: Int, val fieldOfStudy: Int, val studentsStartYear:Int)
