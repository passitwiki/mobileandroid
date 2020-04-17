package com.passitwiki.passit.model

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
