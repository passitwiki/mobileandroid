package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class News(
    val id: Int,
    val title: String,
    val content: String,
    val subject_group: Int,
    val field_age_group: Int,
    val attachment: String?,
    val is_owner: Boolean,
    val created_by: String,
    val modified_by: String,
    val created_at: String,
    val updated_at: String
)
