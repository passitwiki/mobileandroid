package com.passitwiki.passit.model

/**
 * Endpoint dataclass.
 */
data class Lecturer(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val title: String,
    val contact: String,
    val consultations: String
)