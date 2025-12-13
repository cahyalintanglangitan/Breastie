package com.example.breastieproject.ui.screens.community.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.data.repository.dummy.DummyCommunityData
import com.example.breastieproject.ui.screens.community.components.CommunityCard
import com.example.breastieproject.ui.theme.BackupTheme
import com.example.breastieproject.ui.screens.community.components.AnnouncementBanner


@Composable
fun ExploreTab(
    communities: List<Community> = DummyCommunityData.getAvailableCommunities(),  // Keep default
    onJoinCommunity: (Community) -> Unit = {},
    onBannerClick: () -> Unit = {}
){
    var searchQuery by remember { mutableStateOf("") }

    // Filter communities by search query
    val filteredCommunities = remember(searchQuery, communities) {
        if (searchQuery.isEmpty()) {
            communities
        } else {
            communities.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // ✅ Announcement Banner
        item {
            AnnouncementBanner(
                onBannerClick = onBannerClick  // ✅ Pass callback!
            )
        }
        // Search Bar
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        }

        // Available Community Cards
        items(
            items = filteredCommunities,
            key = { it.id }
        ) { community ->
            CommunityCard(
                community = community,
                buttonText = "Gabung",  // ← Different button text!
                onButtonClick = { onJoinCommunity(community) }
            )
        }

        // Empty State
        if (filteredCommunities.isEmpty()) {
            item {
                EmptyState(searchQuery = searchQuery)
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        placeholder = {
            Text(
                text = "Search...",
                color = Color(0xFF999999)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF999999)
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = Color(0xFFFFB8E0),
            unfocusedBorderColor = Color(0xFFFFDFF0),
            // ✅ ADD THESE!
            focusedTextColor = Color(0xFF333333),      // Hitam!
            unfocusedTextColor = Color(0xFF333333),    // Hitam!
            cursorColor = Color(0xFFEC7FA9)            // Pink cursor
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Composable
private fun EmptyState(searchQuery: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (searchQuery.isEmpty()) {
                "Belum ada komunitas tersedia"
            } else {
                "Tidak ditemukan komunitas\n\"$searchQuery\""
            },
            fontSize = 16.sp,
            color = Color(0xFF999999),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExploreTabPreview() {
    BackupTheme {
        ExploreTab()
    }
}


/**
 * ============================================================================
 * FILE: ExploreTab.kt
 * LOCATION: ui/screens/community/tabs/ExploreTab.kt
 * ============================================================================
 *
 * DESKRIPSI SIMPLE:
 * Tab untuk explore & join komunitas BARU yang belum di-join.
 * Kayak "Discover" - user bisa lihat komunitas lain dan join.
 *
 * ============================================================================
 * FUNGSI APA?
 * ============================================================================
 *
 * Menampilkan:
 *   ✅ Banner webinar di atas
 *   ✅ List komunitas yang BELUM di-join
 *   ✅ Button "Join" di tiap komunitas
 *
 * User bisa:
 *   ✅ Browse komunitas available
 *   ✅ Klik "Join" → Masuk komunitas
 *   ✅ Klik banner → Lihat webinar
 *
 * ============================================================================
 * CARA KERJA
 * ============================================================================
 *
 * 1. Parent kasih list komunitas yang BELUM di-join
 * 2. Tab tampilkan pakai LazyColumn
 * 3. Tiap card punya button "Join"
 * 4. User klik "Join" → Callback onJoinCommunity dipanggil
 * 5. Parent tambahkan community.id ke joinedCommunityIds
 * 6. Komunitas pindah dari Explore ke My Community
 *
 * ============================================================================
 * PARAMETER
 * ============================================================================
 *
 * communities: List<Community>
 *   - List komunitas yang BELUM di-join
 *   - Filtered dari DummyCommunityData.getAvailableCommunities()
 *
 * onJoinCommunity: (Community) -> Unit
 *   - Fungsi dipanggil saat user klik "Join"
 *   - Parent update joinedCommunityIds
 *   - Komunitas hilang dari Explore, muncul di My Community
 *
 * onBannerClick: () -> Unit
 *   - Fungsi saat klik banner webinar
 *
 * ============================================================================
 * CONTOH PAKAI
 * ============================================================================
 *
 * // Di CommunityScreen.kt
 *
 * // 1. Filter komunitas yang belum di-join
 * val availableCommunities = DummyCommunityData.communities
 *     .filter { it.id !in joinedCommunityIds }
 *
 * // 2. Show tab
 * ExploreTab(
 *     communities = availableCommunities,
 *     onJoinCommunity = { community ->
 *         // Tambahkan ke joined list
 *         joinedCommunityIds.add(community.id)
 *         println("Joined: ${community.name}")
 *     },
 *     onBannerClick = {
 *         showWebinar = true
 *     }
 * )
 *
 * ============================================================================
 * STRUKTUR UI
 * ============================================================================
 *
 * ┌─────────────────────────────────┐
 * │ [Banner Webinar]                │
 * ├─────────────────────────────────┤
 * │ ● Fitness & Recovery    [Join]  │  ← CommunityCard dengan button
 * │   967 members                   │
 * │   Olahraga ringan...            │
 * ├─────────────────────────────────┤
 * │ ● Keluarga Pendukung    [Join]  │
 * │   1,423 members                 │
 * │   Untuk keluarga...             │
 * ├─────────────────────────────────┤
 * │ ● Nutrisi & Resep       [Join]  │
 * │   2,156 members                 │
 * │   Resep sehat...                │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * JOIN FLOW (Alur Join Komunitas)
 * ============================================================================
 *
 * Step 1: User klik button "Join"
 * Step 2: onJoinCommunity(community) dipanggil
 * Step 3: Parent update state:
 *         joinedCommunityIds.add(community.id)
 * Step 4: UI auto-update:
 *         - Komunitas hilang dari Explore tab
 *         - Komunitas muncul di My Community tab
 * Step 5: User bisa chat di komunitas baru
 *
 * ============================================================================
 * KAPAN PAKAI?
 * ============================================================================
 *
 * Tab ini dipilih saat:
 *   - User tap "Explore" tab (index 1)
 *   - User mau cari komunitas baru
 *   - User mau join komunitas tambahan
 *
 * ============================================================================
 * HUBUNGAN DENGAN FILE LAIN
 * ============================================================================
 *
 * Dipanggil oleh:
 *   - CommunityScreen.kt (tab index = 1)
 *
 * Pakai komponen:
 *   - AnnouncementBanner.kt
 *   - CommunityCard.kt (variant dengan Join button)
 *
 * Pakai data:
 *   - DummyCommunityData.getAvailableCommunities()
 *
 * ============================================================================
 * FUTURE (Firebase)
 * ============================================================================
 *
 * Join action akan jadi:
 *   1. Add to Firestore "community_members" collection
 *   2. Increment community.memberCount
 *   3. Real-time update di semua device
 *
 * Query jadi:
 *   - Get all communities WHERE id NOT IN user's joined list
 *
 * ============================================================================
 * TIPS
 * ============================================================================
 *
 * Test join:
 *   1. Buka Explore tab
 *   2. Klik "Join" di komunitas manapun
 *   3. Switch ke "My Community" tab
 *   4. Komunitas yang di-join muncul!
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * STATUS: ✅ COMPLETE
 * ============================================================================
 */