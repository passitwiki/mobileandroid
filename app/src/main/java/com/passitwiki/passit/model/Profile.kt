package com.passitwiki.passit.model

/**
 * User endpoint data class.
 */
data class Profile(
    val memberships: List<Memberships>,
    val field_age_groups: List<FieldAgeGroup>
)