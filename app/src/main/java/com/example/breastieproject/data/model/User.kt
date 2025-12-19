/**
 * ============================================================================
 * FILE: User.kt
 * LOCATION: data/model/User.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI:
 * Data model untuk user information.
 * Dipakai untuk: Authentication, Profile, Community (anonymous).
 *
 * PROPERTIES:
 * - userId: Firebase Auth UID
 * - fullName: Real name (private, cuma di profile)
 * - email: Email address
 * - anonymousName: Anonymous name (public di community)
 * - dateOfBirth: Optional (untuk reminder & personalization)
 * - profilePhotoUrl: Firebase Storage URL (optional)
 * - createdAt: Timestamp saat sign up
 *
 * USAGE:
 * - AuthViewModel: Sign up/in
 * - ProfileScreen: Display & edit user data
 * - CommunityScreen: Use anonymousName for posts
 *
 * FIREBASE MAPPING:
 * Collection: users/{userId}
 * Document fields: Same as properties
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 17 Dec 2024
 * UPDATED: 17 Dec 2024
 * STATUS: ✅ Complete
 * ============================================================================
 */

package com.example.breastieproject.data.model

/**
 * User data model
 */
data class User(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val anonymousName: String = "",      // ✅ For community (e.g., "Brave Warrior")
    val dateOfBirth: String = "",
    val profilePhotoUrl: String = "",    // ✅ Firebase Storage URL
    val createdAt: Long = 0



)