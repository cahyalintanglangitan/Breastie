package com.example.breastieproject.data.model

//import com.google.firebase.Timestamp
data class CheckUpFAQ(
    val id: String = "",
    val question: String = "",
    val category: String = "",
    val order: Int = 0,
    val isActive: Boolean = true,
)