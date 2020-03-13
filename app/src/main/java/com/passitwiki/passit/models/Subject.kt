package com.passitwiki.passit.models

/**
 * Endpoint dataclass.
 */
data class Subject(
    val id: Int,
    val name: String,
    val semester: Int,
    val general_description: String,
    val field_of_study: String
)