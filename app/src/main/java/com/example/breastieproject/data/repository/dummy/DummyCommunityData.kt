package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.Community

object DummyCommunityData {

    // Dummy communities (sesuai screenshot kamu!)
    val communities = listOf(
        Community(
            id = "comm_001",
            name = "Pejuang Kemo",
            description = "Komunitas untuk saling mendukung selama proses kemoterapi",
            memberCount = 1247,
            avatarUrl = "", // Nanti pakai placeholder icon
            isActive = true,
            upcomingEvent = "Akan ada event edukasi minggu ini!",
            category = "support"
        ),
        Community(
            id = "comm_002",
            name = "Survivor Stories",
            description = "Komunitas untuk saling berbagi cerita inspiratif para penyintas kanker payudara/ibukanker selama proses kemoterapi",
            memberCount = 1247,
            avatarUrl = "",
            isActive = true,
            upcomingEvent = "Sesi sharing minggu depan",
            category = "survivor"
        ),
        Community(
            id = "comm_003",
            name = "Healing Together",
            description = "Komunitas untuk healing mental dan spiritual bersama",
            memberCount = 1247,
            avatarUrl = "",
            isActive = true,
            upcomingEvent = "Meditasi bersama besok",
            category = "support"
        ),
        Community(
            id = "comm_004",
            name = "Fitness & Recovery",
            description = "Olahraga ringan dan pemulihan fisik pasca operasi",
            memberCount = 1247,
            avatarUrl = "",
            isActive = true,
            upcomingEvent = "Akan ada event berbagi tips olahraga setiap bulan",
            category = "health"
        ),
        Community(
            id = "comm_005",
            name = "Keluarga Pendukung",
            description = "Komunitas untuk keluarga dan teman yang mendampingi",
            memberCount = 1247,
            avatarUrl = "",
            isActive = true,
            upcomingEvent = "Akan ada event berbagi kisah pemulihan setiap bulan",
            category = "family"
        ),
        Community(
            id = "comm_006",
            name = "Nutrisi & Resep",
            description = "Berbagi resep sehat dan tips nutrisi untuk pemulihan",
            memberCount = 1247,
            avatarUrl = "",
            isActive = true,
            upcomingEvent = "Workshop masak sehat minggu ini",
            category = "nutrition"
        )
    )

    // Dummy: User sudah join komunitas mana aja
    val userJoinedCommunityIds = listOf(
        "comm_001", // Pejuang Kemo
        "comm_002", // Survivor Stories
        "comm_003"  // Healing Together
    )

    // Function: Get communities yang user sudah join
    fun getJoinedCommunities(): List<Community> {
        return communities.filter { it.id in userJoinedCommunityIds }
    }

    // Function: Get communities yang belum di-join
    fun getAvailableCommunities(): List<Community> {
        return communities.filter { it.id !in userJoinedCommunityIds }
    }
}


/**
 * ============================================================================
 * FILE: DummyCommunityData.kt
 * LOCATION: data/repository/dummy/DummyCommunityData.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Temporary data source untuk development sebelum Firebase integration.
 * Menyediakan dummy data komunitas & helper functions.
 *
 * ⚠️ IMPORTANT: File ini akan DIHAPUS saat integrasi Firebase!
 *
 * ============================================================================
 * DATA
 * ============================================================================
 *
 * communities: List<Community>
 *   - 6 komunitas dummy dengan berbagai kategori
 *   - Data lengkap dengan member count, event, dll
 *   - Immutable list (val)
 *
 * userJoinedCommunityIds: List<String>
 *   - IDs komunitas yang user sudah join
 *   - Default: 3 komunitas (comm_001, comm_002, comm_003)
 *   - Simulasi user state
 *
 * ============================================================================
 * FUNCTIONS
 * ============================================================================
 *
 * getJoinedCommunities(): List<Community>
 *   - Return list komunitas yang user sudah join
 *   - Filter berdasarkan userJoinedCommunityIds
 *   - Dipakai di: MyCommunityTab.kt
 *
 * getAvailableCommunities(): List<Community>
 *   - Return list komunitas yang belum di-join
 *   - Filter exclude userJoinedCommunityIds
 *   - Dipakai di: ExploreTab.kt
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Get all communities
 * val allCommunities = DummyCommunityData.communities
 *
 * // Get joined communities
 * val joined = DummyCommunityData.getJoinedCommunities()
 *
 * // Get available communities
 * val available = DummyCommunityData.getAvailableCommunities()
 *
 * // Find specific community
 * val community = DummyCommunityData.communities
 *     .find { it.id == "comm_001" }
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - CommunityScreen.kt (main container)
 * - MyCommunityTab.kt (joined list)
 * - ExploreTab.kt (explore list)
 * - ChatScreen.kt (community detail)
 *
 * ============================================================================
 * MIGRATION TO FIREBASE
 * ============================================================================
 *
 * Saat Firebase ready:
 *
 * 1. Create CommunityRepository.kt
 * 2. Implement functions:
 *    - getAllCommunities()
 *    - getJoinedCommunities(userId)
 *    - joinCommunity(userId, communityId)
 *    - leaveCommunity(userId, communityId)
 *
 * 3. Replace all DummyCommunityData calls:
 *    BEFORE: DummyCommunityData.communities
 *    AFTER:  communityRepository.getAllCommunities()
 *
 * 4. Delete this file
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Object singleton (bukan class)
 * - Data hard-coded for testing
 * - Member counts static (gak update otomatis)
 * - Join/leave action cuma log, gak persist
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * STATUS: ⚠️ TEMPORARY - Will be replaced by Firebase
 * ============================================================================
 */