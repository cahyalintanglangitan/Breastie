package com.example.breastieproject.ui.screens.community.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.ui.screens.community.components.AnnouncementBanner
import com.example.breastieproject.ui.screens.community.components.PostCard
import com.example.breastieproject.ui.theme.BackupTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedTab(
    posts: List<Post> = emptyList(),
    likedPostIds: Set<String> = emptySet(),
    currentUserId: String = "",
    joinedCommunities: List<Community> = emptyList(),  // ✅ ADD THIS!
    onLikeClick: (Post) -> Unit = {},
    onCommentClick: (Post) -> Unit = {},
    onDeleteClick: (Post) -> Unit = {},
    onBannerClick: () -> Unit = {}
) {

    // ✅ ADD FILTER STATE:
    var selectedCommunityId by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // ✅ FILTER POSTS:
    val filteredPosts = if (selectedCommunityId == null) {
        posts  // Show all
    } else {
        posts.filter { it.communityId == selectedCommunityId }  // Filter by community
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA)),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Announcement Banner
        item {
            AnnouncementBanner(
                onBannerClick = onBannerClick
            )
        }

        // Post Cards
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = if (selectedCommunityId == null) {
                            "All Communities"
                        } else {
                            joinedCommunities.find { it.id == selectedCommunityId }?.name
                                ?: "All Communities"
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Filter by Community") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFEC7FA9),
                            unfocusedBorderColor = Color(0xFFFFB8E0)
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        // "All Communities" option
                        DropdownMenuItem(
                            text = { Text("All Communities") },
                            onClick = {
                                selectedCommunityId = null
                                expanded = false
                            }
                        )

                        // Individual communities
                        joinedCommunities.forEach { community ->
                            DropdownMenuItem(
                                text = { Text(community.name) },
                                onClick = {
                                    selectedCommunityId = community.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Post Cards (✅ USE FILTERED POSTS!)
        items(
            items = filteredPosts,  // ✅ CHANGED from 'posts'
            key = { it.id }
        ) { post ->
            PostCard(
                post = post,
                isLiked = post.id in likedPostIds,
                currentUserId = currentUserId,
                onLikeClick = { onLikeClick(post) },
                onCommentClick = { onCommentClick(post) },
                onDeleteClick = { onDeleteClick(post) }
            )
        }

        // Empty State (✅ USE FILTERED POSTS!)
        if (filteredPosts.isEmpty()) {
            item {
                EmptyState()
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your feed is empty for now\nTap New Post to create your first post!",
            fontSize = 16.sp,
            color = Color(0xFF999999),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FeedTabPreview() {
    BackupTheme {
        FeedTab()
    }
}

/**
 * ============================================================================
 * FILE: FeedTab.kt
 * LOCATION: ui/screens/community/tabs/FeedTab.kt
 * ============================================================================
 *
 * DESKRIPSI SIMPLE:
 * Tab feed social media - kayak Instagram/Facebook feed.
 * Tampilkan semua post dari komunitas yang user join.
 *
 * ============================================================================
 * FUNGSI APA?
 * ============================================================================
 *
 * Menampilkan:
 *   ✅ Banner webinar di atas
 *   ✅ List semua post dari komunitas yang di-join
 *   ✅ Like, comment, share buttons
 *
 * User bisa:
 *   ✅ Scroll feed (infinite scroll - future)
 *   ✅ Like post
 *   ✅ Klik comment → Lihat & tulis komentar
 *   ✅ Share post (future)
 *
 * ============================================================================
 * CARA KERJA
 * ============================================================================
 *
 * 1. Parent kasih list posts dari feed
 * 2. Tab tampilkan pakai LazyColumn
 * 3. Tiap post pakai PostCard component
 * 4. User interaksi (like/comment/share)
 * 5. Callback dipanggil, parent update data
 *
 * ============================================================================
 * PARAMETER
 * ============================================================================
 *
 * posts: List<Post>
 *   - List semua post di feed
 *   - Dari DummyPostData.getFeedPosts()
 *   - Sorted by newest first
 *
 * onLikeClick: (Post) -> Unit
 *   - Fungsi saat user like/unlike post
 *   - Parent toggle like state
 *   - Update likeCount
 *
 * onCommentClick: (Post) -> Unit
 *   - Fungsi saat user klik button comment
 *   - Parent show CommentScreen
 *   - Pass post data
 *
 * onShareClick: (Post) -> Unit
 *   - Fungsi saat user klik share (future)
 *   - Show share dialog
 *
 * onBannerClick: () -> Unit
 *   - Fungsi saat klik banner
 *
 * ============================================================================
 * CONTOH PAKAI
 * ============================================================================
 *
 * // Di CommunityScreen.kt
 *
 * // 1. Load posts
 * var feedPosts by remember {
 *     mutableStateOf(DummyPostData.getFeedPosts().toMutableList())
 * }
 *
 * // 2. Show tab
 * FeedTab(
 *     posts = feedPosts,
 *     onLikeClick = { post ->
 *         // Toggle like (dummy - just log)
 *         println("Liked: ${post.id}")
 *     },
 *     onCommentClick = { post ->
 *         selectedPost = post
 *         showComments = true  // Buka CommentScreen
 *     },
 *     onShareClick = { post ->
 *         println("Share: ${post.id}")
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
 * │ ● Member A · Pejuang Kemo       │  ← PostCard 1
 * │   2 jam yang lalu               │
 * │                                 │
 * │ Hari ini saya selesai sesi...   │
 * │                                 │
 * │ ❤️ 24  💬 8  📤 3              │
 * │ ❤️ Like  💬 Comment  📤 Share  │
 * ├─────────────────────────────────┤
 * │ ● Member B · Survivor Stories   │  ← PostCard 2
 * │   5 jam yang lalu               │
 * │                                 │
 * │ Alhamdulillah hari ini...       │
 * │                                 │
 * │ ❤️ 156  💬 24  📤 12           │
 * │ ❤️ Like  💬 Comment  📤 Share  │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * INTERACTION FLOW
 * ============================================================================
 *
 * LIKE:
 *   User klik ❤️ Like button
 *   → onLikeClick(post) dipanggil
 *   → Parent toggle like
 *   → Icon berubah warna (gray → pink)
 *   → likeCount +1 atau -1
 *
 * COMMENT:
 *   User klik 💬 Comment button
 *   → onCommentClick(post) dipanggil
 *   → Parent show CommentScreen
 *   → User bisa lihat & tulis comment
 *   → commentCount update saat comment ditambah
 *
 * SHARE:
 *   User klik 📤 Share button
 *   → onShareClick(post) dipanggil
 *   → Show share options (future)
 *
 * ============================================================================
 * KAPAN PAKAI?
 * ============================================================================
 *
 * Tab ini dipilih saat:
 *   - User tap "Feed" tab (index 2)
 *   - User mau lihat update dari semua komunitas
 *   - User mau like/comment posts
 *
 * ============================================================================
 * HUBUNGAN DENGAN FILE LAIN
 * ============================================================================
 *
 * Dipanggil oleh:
 *   - CommunityScreen.kt (tab index = 2)
 *
 * Pakai komponen:
 *   - AnnouncementBanner.kt
 *   - PostCard.kt (tiap post)
 *
 * Pakai data:
 *   - DummyPostData.getFeedPosts()
 *
 * Navigate ke:
 *   - CommentScreen.kt (via callback)
 *
 * ============================================================================
 * FUTURE (Firebase)
 * ============================================================================
 *
 * Real-time feed:
 *   - Firestore listener untuk posts
 *   - Auto-update saat ada post baru
 *   - Pagination (load 20 posts per page)
 *   - Pull-to-refresh
 *
 * Query:
 *   collection("posts")
 *     .where("communityId", "in", userJoinedCommunityIds)
 *     .orderBy("timestamp", "desc")
 *     .limit(20)
 *
 * Like action:
 *   - Update Firestore post.likeCount
 *   - Add userId to post.likedBy array
 *   - Optimistic UI update
 *
 * ============================================================================
 * TIPS
 * ============================================================================
 *
 * Test feed:
 *   1. Buka Feed tab
 *   2. Scroll lihat semua posts
 *   3. Klik like → Icon berubah warna
 *   4. Klik comment → Masuk CommentScreen
 *
 * Tambah post baru:
 *   - Klik "+ Buat Post" button
 *   - Isi form, post
 *   - Post muncul di top feed!
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * STATUS: ✅ COMPLETE
 * ============================================================================
 */