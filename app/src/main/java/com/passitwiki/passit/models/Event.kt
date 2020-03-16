package com.passitwiki.passit.models

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val category: String,
    val due_date: String,
    val field_age_group: Int,
    val subjectGroup: Int,
    val created_by: String,
    val modified_by: String
)