package com.passitwiki.passit.model

/**
 * User endpoint data class.
 */
data class Memberships(
    val id: Int,
    val profile: Int,
    val field_age_group: Int,
    val type: Int,
    val is_default: Boolean
)