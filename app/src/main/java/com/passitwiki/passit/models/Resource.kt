package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class Resource(
    val id: Int,
    val name: String,
    val image: String?,
    val url: String,
    val description: String,
    val subject: Int,
    val created_by: Int?,
    val modified_by: Int?
)