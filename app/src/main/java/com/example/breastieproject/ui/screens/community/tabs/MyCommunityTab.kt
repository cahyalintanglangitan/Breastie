package com.example.breastieproject.ui.screens.community.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.data.repository.dummy.DummyCommunityData
import com.example.breastieproject.ui.screens.community.components.CommunityCard
import com.example.breastieproject.ui.theme.BackupTheme
import com.example.breastieproject.ui.screens.community.components.AnnouncementBanner


@Composable
fun MyCommunityTab(
    communities: List<Community> = DummyCommunityData.getJoinedCommunities(),  // Keep default
    onCommunityClick: (Community) -> Unit = {},
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
        // Announcement Banner (Simplified)
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

        // Community Cards
        items(
            items = filteredCommunities,
            key = { it.id }
        ) { community ->
            CommunityCard(
                community = community,
                buttonText = "Lihat",
                onButtonClick = { onCommunityClick(community) }
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
private fun SimplifiedAnnouncementBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4F2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Innovative Ideas from\nWomen Entrepreneurs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Get inspired by success stories of women entrepreneurs in technology",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Row: Date + Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Webinar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFEC7FA9)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "8 March 2025",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Register Button
                Button(
                    onClick = { /* TODO: Handle register */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Register now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
                "Belum ada komunitas"
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
fun MyCommunityTabPreview() {
    BackupTheme {
        MyCommunityTab()
    }
}

/**
 * ============================================================================
 * FILE: MyCommunityTab.kt
 * LOCATION: ui/screens/community/tabs/MyCommunityTab.kt
 * ============================================================================
 *
 * DESKRIPSI SIMPLE:
 * Tab yang menampilkan daftar komunitas yang SUDAH user join.
 * Kayak WhatsApp group list - klik komunitas langsung masuk chat.
 *
 * ============================================================================
 * FUNGSI APA?
 * ============================================================================
 *
 * Menampilkan:
 *   ✅ Banner webinar di atas
 *   ✅ List komunitas yang user sudah join
 *   ✅ Setiap komunitas punya nama, member count, event info
 *
 * User bisa:
 *   ✅ Klik komunitas → Masuk ChatScreen
 *   ✅ Klik banner → Lihat detail webinar
 *   ✅ Scroll kalau banyak komunitas
 *
 * ============================================================================
 * CARA KERJA
 * ============================================================================
 *
 * 1. Parent (CommunityScreen) kasih data komunitas yang udah di-join
 * 2. Tab ini tampilkan pakai LazyColumn (list yang bisa scroll)
 * 3. Tiap komunitas ditampilkan pakai CommunityCard component
 * 4. User klik card → Callback onCommunityClick dipanggil
 * 5. Parent handle navigation ke ChatScreen
 *
 * ============================================================================
 * PARAMETER (Input dari parent)
 * ============================================================================
 *
 * communities: List<Community>
 *   - List komunitas yang user sudah join
 *   - Dikasih sama CommunityScreen
 *   - Difilter dari DummyCommunityData.getJoinedCommunities()
 *
 * onCommunityClick: (Community) -> Unit
 *   - Fungsi yang dipanggil saat user klik komunitas
 *   - Parent akan show ChatScreen
 *   - Contoh: onCommunityClick = { community -> showChat(community) }
 *
 * onBannerClick: () -> Unit
 *   - Fungsi yang dipanggil saat user klik banner webinar
 *   - Parent akan show WebinarDetailScreen
 *
 * ============================================================================
 * CONTOH PAKAI
 * ============================================================================
 *
 * // Di CommunityScreen.kt
 *
 * // 1. Prepare data (filter joined communities)
 * val joinedCommunities = DummyCommunityData.communities
 *     .filter { it.id in joinedCommunityIds }
 *
 * // 2. Show tab
 * MyCommunityTab(
 *     communities = joinedCommunities,  // Kasih data
 *     onCommunityClick = { community ->
 *         selectedCommunity = community
 *         showChat = true  // Buka ChatScreen
 *     },
 *     onBannerClick = {
 *         showWebinar = true  // Buka WebinarDetailScreen
 *     }
 * )
 *
 * ============================================================================
 * STRUKTUR UI (Tampilan)
 * ============================================================================
 *
 * ┌─────────────────────────────────┐
 * │ [Banner Webinar]                │  ← AnnouncementBanner
 * ├─────────────────────────────────┤
 * │ ● Pejuang Kemo          ⚡      │  ← CommunityCard 1
 * │   1,247 members                 │
 * │   Support selama kemo...        │
 * ├─────────────────────────────────┤
 * │ ● Survivor Stories      ⚡      │  ← CommunityCard 2
 * │   2,834 members                 │
 * │   Berbagi cerita inspiratif...  │
 * ├─────────────────────────────────┤
 * │ ● Healing Together      ⚡      │  ← CommunityCard 3
 * │   1,532 members                 │
 * │   Healing mental...             │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * KAPAN PAKAI?
 * ============================================================================
 *
 * Tab ini dipilih saat:
 *   - User tap "My Community" tab (index 0)
 *   - User buka CommunityScreen (default tab)
 *   - User mau lihat komunitas yang udah di-join
 *
 * ============================================================================
 * HUBUNGAN DENGAN FILE LAIN
 * ============================================================================
 *
 * Dipanggil oleh:
 *   - CommunityScreen.kt (saat tab index = 0)
 *
 * Memanggil komponen:
 *   - AnnouncementBanner.kt (banner webinar)
 *   - CommunityCard.kt (tiap komunitas)
 *
 * Pakai data dari:
 *   - DummyCommunityData.getJoinedCommunities()
 *
 * Navigate ke:
 *   - ChatScreen.kt (via callback)
 *   - WebinarDetailScreen.kt (via callback)
 *
 * ============================================================================
 * FUTURE (Nanti kalau pakai Firebase)
 * ============================================================================
 *
 * Data source berubah dari:
 *   ❌ DummyCommunityData.getJoinedCommunities()
 *
 * Jadi:
 *   ✅ Firestore query:
 *      collection("community_members")
 *        .where("userId", "==", currentUserId)
 *        .get()
 *
 * Real-time update:
 *   - Kalau ada yang join komunitas baru → Langsung muncul
 *   - Kalau ada yang leave → Langsung hilang
 *   - Member count update otomatis
 *
 * ============================================================================
 * TIPS UNTUK TIM
 * ============================================================================
 *
 * Kalau mau test:
 *   1. Run app
 *   2. Tap tab "Community"
 *   3. Tab "My Community" selected by default
 *   4. Klik salah satu komunitas → Masuk chat
 *
 * Kalau mau tambah komunitas joined:
 *   - Edit DummyCommunityData.userJoinedCommunityIds
 *   - Tambah ID komunitas yang mau di-join
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * STATUS: ✅ COMPLETE
 * ============================================================================
 */