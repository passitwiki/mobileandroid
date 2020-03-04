package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class News(
    val id: Int,
    val title: String,
    val content: String,
    val subject_group: Int,
    val field_age_group: Int
)
