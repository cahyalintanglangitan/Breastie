package com.example.breastieproject.data.model

data class WebinarEvent(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val date: String = "",
    val location: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val whatToExpect: String = "",
    val registrationDetails: String = ""
)

/**
 * ============================================================================
 * FILE: WebinarEvent.kt
 * LOCATION: data/model/WebinarEvent.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Data class untuk merepresentasikan event webinar edukasi.
 * Banner webinar ditampilkan di semua tabs community.
 *
 * ============================================================================
 * PROPERTIES
 * ============================================================================
 *
 * id: String - Unique identifier webinar
 * title: String - Judul webinar
 * description: String - Deskripsi lengkap
 * date: String - Tanggal webinar ("June 5, 2025")
 * type: String - Tipe event ("Webinar", "Workshop", dll)
 * imageUrl: String - Banner image URL
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * val webinar = WebinarEvent(
 *     id = "webinar_001",
 *     title = "Innovative Ideas from Women Entrepreneurs",
 *     description = "Get inspired by success stories...",
 *     date = "June 5, 2025",
 *     type = "Webinar",
 *     imageUrl = ""
 * )
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - WebinarDetailScreen.kt
 * - AnnouncementBanner.kt (component)
 * - All tabs (My Community, Explore, Feed)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */