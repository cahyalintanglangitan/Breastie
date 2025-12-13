package com.example.breastieproject.data.model

data class WebinarRegistration(
    val id: String = "",
    val webinarId: String = "",
    val userEmail: String = "",
    val registeredAt: Long = System.currentTimeMillis(),
    val status: String = "registered" // "registered", "attended", "cancelled"
)