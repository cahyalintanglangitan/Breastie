package com.example.breastieproject.data.model

data class User(
    val id: String = "",
    val realName: String = "",
    val communityUsername: String = "", // Anonymous username
    val email: String = "",
    val profilePictureUrl: String = "",
    val joinedCommunities: List<String> = emptyList() // List of community IDs
)