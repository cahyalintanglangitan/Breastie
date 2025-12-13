package com.example.breastieproject.data.model

data class Community(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val memberCount: Int = 0,
    val avatarUrl: String = "",
    val isActive: Boolean = true,
    val upcomingEvent: String = "",
    val category: String = "" // e.g., "support", "education", "survivor"
)


/**
 * ============================================================================
 * FILE: Community.kt
 * LOCATION: data/model/Community.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Data class untuk merepresentasikan komunitas support kanker payudara.
 * Model ini digunakan untuk menyimpan informasi komunitas yang bisa di-join
 * oleh user untuk mendapatkan support dan berbagi pengalaman.
 *
 * ============================================================================
 * PROPERTIES
 * ============================================================================
 *
 * id: String
 *   - Unique identifier untuk komunitas (e.g., "comm_001")
 *   - Digunakan untuk tracking join/leave komunitas
 *   - Format: "comm_XXX"
 *
 * name: String
 *   - Nama komunitas yang ditampilkan di UI
 *   - Contoh: "Pejuang Kemo", "Survivor Stories"
 *   - Max length: 50 karakter (recommended)
 *
 * description: String
 *   - Deskripsi singkat tujuan/fokus komunitas
 *   - Ditampilkan di card komunitas
 *   - Max length: 150 karakter (recommended)
 *
 * memberCount: Int
 *   - Jumlah member yang sudah join
 *   - Ditampilkan dengan format "1,247 members"
 *   - Update otomatis saat ada yang join/leave
 *
 * avatarUrl: String
 *   - URL avatar/logo komunitas
 *   - Empty string ("") = gunakan placeholder pink circle
 *   - Future: URL dari Firebase Storage
 *
 * isActive: Boolean
 *   - Status aktif komunitas
 *   - true = komunitas bisa di-join
 *   - false = komunitas archived/tidak aktif
 *
 * upcomingEvent: String
 *   - Info event mendatang komunitas
 *   - Ditampilkan di card sebagai highlight
 *   - Contoh: "Akan ada event edukasi minggu ini!"
 *
 * category: String
 *   - Kategori komunitas untuk filtering
 *   - Values: "support", "survivor", "health", "family", "nutrition"
 *   - Future: Bisa jadi enum class
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Create instance
 * val community = Community(
 *     id = "comm_001",
 *     name = "Pejuang Kemo",
 *     description = "Komunitas untuk saling mendukung selama kemoterapi",
 *     memberCount = 1247,
 *     avatarUrl = "",
 *     isActive = true,
 *     upcomingEvent = "Event edukasi minggu ini!",
 *     category = "support"
 * )
 *
 * // Access properties
 * println(community.name)  // "Pejuang Kemo"
 * println(community.memberCount)  // 1247
 *
 * // Copy with changes (immutable)
 * val updatedCommunity = community.copy(memberCount = 1248)
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - DummyCommunityData.kt (data source dummy)
 * - CommunityCard.kt (display UI)
 * - MyCommunityTab.kt (joined communities list)
 * - ExploreTab.kt (available communities list)
 * - ChatScreen.kt (group chat header)
 *
 * ============================================================================
 * FIREBASE MAPPING (FUTURE)
 * ============================================================================
 *
 * Firestore Collection: "communities"
 * Document ID: community.id
 *
 * Fields mapping:
 * {
 *   "id": "comm_001",
 *   "name": "Pejuang Kemo",
 *   "description": "...",
 *   "memberCount": 1247,
 *   "avatarUrl": "https://...",
 *   "isActive": true,
 *   "upcomingEvent": "...",
 *   "category": "support",
 *   "createdAt": Timestamp,
 *   "updatedAt": Timestamp
 * }
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Data class otomatis generate: equals(), hashCode(), toString(), copy()
 * - Immutable by default (val properties)
 * - Default values untuk semua properties (Firebase compatibility)
 * - Nanti tambah: createdAt, updatedAt, createdBy
 *
 * ============================================================================
 * AUTHOR: Lintang
 * ============================================================================
 */

