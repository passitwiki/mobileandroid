package com.passitwiki.passit.model

/**
 * Endpoint dataclass.
 */
data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val due_date: String,
    val field_age_group: Int,
    val subject_group: Int?,
    val created_by: String,
    val modified_by: String
)