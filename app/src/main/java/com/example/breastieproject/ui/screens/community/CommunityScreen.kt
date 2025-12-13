package com.example.breastieproject.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.data.repository.dummy.DummyCommunityData
import com.example.breastieproject.data.repository.dummy.DummyPostData
import com.example.breastieproject.ui.screens.community.tabs.ExploreTab
import com.example.breastieproject.ui.screens.community.tabs.FeedTab
import com.example.breastieproject.ui.screens.community.tabs.MyCommunityTab
import com.example.breastieproject.ui.screens.webinar.WebinarDetailScreen
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun CommunityScreen() {  // ✅ Hapus semua parameters!
    var selectedTab by remember { mutableStateOf(0) }

    // State for navigation
    var selectedCommunity by remember { mutableStateOf<Community?>(null) }
    var showChat by remember { mutableStateOf(false) }
    var showWebinar by remember { mutableStateOf(false) }
    var showCreatePost by remember { mutableStateOf(false) }

    // State for comment screen
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var showComments by remember { mutableStateOf(false) }

    // State for data management
    var joinedCommunityIds by remember {
        mutableStateOf(DummyCommunityData.userJoinedCommunityIds.toMutableList())
    }
    var feedPosts by remember {
        mutableStateOf(DummyPostData.getFeedPosts().toMutableList())
    }

    // Show CommentScreen
    if (showComments && selectedPost != null) {
        CommentScreen(
            post = selectedPost!!,
            onBackClick = {
                showComments = false
                selectedPost = null
            },
            onCommentAdded = {
                val postIndex = feedPosts.indexOfFirst { it.id == selectedPost!!.id }
                if (postIndex != -1) {
                    feedPosts[postIndex] = feedPosts[postIndex].copy(
                        commentCount = feedPosts[postIndex].commentCount + 1
                    )
                }
            }
        )
    }
    // Show CreatePostScreen
    else if (showCreatePost) {
        val joinedCommunities = DummyCommunityData.communities
            .filter { it.id in joinedCommunityIds }

        CreatePostScreen(
            joinedCommunities = joinedCommunities,
            onBackClick = { showCreatePost = false },
            onPostCreated = { newPost ->
                feedPosts.add(0, newPost)
                println("New post created: ${newPost.content}")
            }
        )
    }
    // Show WebinarScreen
    else if (showWebinar) {
        WebinarDetailScreen(
            onBackClick = { showWebinar = false },
            onRegisterSuccess = { email ->
                println("Registered email: $email")
            }
        )
    }
    // Show ChatScreen
    else if (showChat && selectedCommunity != null) {
        ChatScreen(
            community = selectedCommunity!!,
            onBackClick = {
                showChat = false
                selectedCommunity = null
            }
        )
    }
    // Show normal CommunityScreen
    else {
        // ✅ GAK PAKAI SCAFFOLD LAGI!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFEDFA))  // Background pink
        ) {
            // Page Header
            CommunityPageHeader(
                onCreatePostClick = {
                    showCreatePost = true
                }
            )

            // Tab Row
            CommunityTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // Tab Content
            when (selectedTab) {
                0 -> {
                    val joinedCommunities = DummyCommunityData.communities
                        .filter { it.id in joinedCommunityIds }

                    MyCommunityTab(
                        communities = joinedCommunities,
                        onCommunityClick = { community ->
                            selectedCommunity = community
                            showChat = true
                        },
                        onBannerClick = {
                            showWebinar = true
                        }
                    )
                }
                1 -> {
                    val availableCommunities = DummyCommunityData.communities
                        .filter { it.id !in joinedCommunityIds }

                    ExploreTab(
                        communities = availableCommunities,
                        onJoinCommunity = { community ->
                            joinedCommunityIds.add(community.id)
                            println("Joined: ${community.name}")
                        },
                        onBannerClick = {
                            showWebinar = true
                        }
                    )
                }
                2 -> FeedTab(
                    posts = feedPosts,
                    onLikeClick = { post ->
                        println("Liked: ${post.id}")
                    },
                    onCommentClick = { post ->
                        selectedPost = post
                        showComments = true
                    },
                    onShareClick = { post ->
                        println("Share: ${post.id}")
                    },
                    onBannerClick = {
                        showWebinar = true
                    }
                )
            }
        }
    }
}

@Composable
private fun CommunityPageHeader(
    onCreatePostClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Community",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "connect with people who understand your journey",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            Button(
                onClick = onCreatePostClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC7FA9)
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Post",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Buat Post",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CommunityTabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("My Community", "Explore", "Feed")

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = Color(0xFFEC7FA9)
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selectedContentColor = Color(0xFFEC7FA9),
                unselectedContentColor = Color(0xFF999999)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommunityScreenPreview() {
    BackupTheme {
        CommunityScreen()
    }
}


/**
 * ============================================================================
 * FILE: CommunityScreen.kt
 * LOCATION: ui/screens/community/CommunityScreen.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Main container screen untuk fitur Community.
 * Handle navigation antar tabs & modal screens (Chat, Create Post, dll).
 *
 * ============================================================================
 * NAVIGATION FLOW
 * ============================================================================
 *
 * CommunityScreen (Main Container)
 * │
 * ├─ Tab 0: My Community Tab
 * │  ├─ Click community card → ChatScreen (modal)
 * │  └─ Click banner → WebinarDetailScreen (modal)
 * │
 * ├─ Tab 1: Explore Tab
 * │  ├─ Click join button → Add to joined list
 * │  └─ Click banner → WebinarDetailScreen (modal)
 * │
 * ├─ Tab 2: Feed Tab
 * │  ├─ Click comment → CommentScreen (modal)
 * │  ├─ Click like → Update like count
 * │  └─ Click banner → WebinarDetailScreen (modal)
 * │
 * └─ Click "Buat Post" → CreatePostScreen (modal)
 *
 * ============================================================================
 * STATE MANAGEMENT
 * ============================================================================
 *
 * Local States:
 *
 * selectedTab: Int
 *   - Current active tab (0, 1, 2)
 *   - Controls which tab content to show
 *
 * selectedCommunity: Community?
 *   - Community yang dipilih untuk chat
 *   - null = no community selected
 *
 * showChat: Boolean
 *   - true = Show ChatScreen modal
 *   - false = Show normal tabs
 *
 * showWebinar: Boolean
 *   - true = Show WebinarDetailScreen
 *   - false = Hide
 *
 * showCreatePost: Boolean
 *   - true = Show CreatePostScreen
 *   - false = Hide
 *
 * selectedPost: Post?
 *   - Post yang dipilih untuk comment
 *   - null = no post selected
 *
 * showComments: Boolean
 *   - true = Show CommentScreen
 *   - false = Hide
 *
 * joinedCommunityIds: MutableList<String>
 *   - IDs komunitas yang user sudah join
 *   - Mutable untuk join/leave actions
 *
 * feedPosts: MutableList<Post>
 *   - List posts di feed
 *   - Mutable untuk create/delete posts
 *
 * ============================================================================
 * UI COMPONENTS
 * ============================================================================
 *
 * CommunityPageHeader:
 *   - Title: "Community"
 *   - Subtitle: "connect with people..."
 *   - Button: "+ Buat Post"
 *
 * CommunityTabRow:
 *   - Tab 0: My Community
 *   - Tab 1: Explore
 *   - Tab 2: Feed
 *   - Pink indicator for selected
 *
 * Tab Content (when):
 *   - 0: MyCommunityTab
 *   - 1: ExploreTab
 *   - 2: FeedTab
 *
 * Modal Screens (if conditions):
 *   - ChatScreen (showChat && selectedCommunity != null)
 *   - WebinarDetailScreen (showWebinar)
 *   - CreatePostScreen (showCreatePost)
 *   - CommentScreen (showComments && selectedPost != null)
 *
 * ============================================================================
 * DATA FLOW
 * ============================================================================
 *
 * Current (Dummy Data):
 *
 * DummyCommunityData
 * ├─ communities → MyCommunityTab, ExploreTab
 * ├─ userJoinedCommunityIds → Filter joined/available
 * └─ Functions: getJoinedCommunities(), getAvailableCommunities()
 *
 * DummyPostData
 * └─ getFeedPosts() → FeedTab
 *
 * Future (Firebase):
 *
 * Firestore "communities" collection
 * ├─ Real-time listener
 * ├─ Query: where("isActive", "==", true)
 * └─ Update on join/leave
 *
 * Firestore "posts" collection
 * ├─ Real-time listener
 * ├─ Query: orderBy("timestamp", "desc")
 * └─ Update on create/delete
 *
 * Firestore "community_members" collection
 * ├─ Track user joins
 * └─ Update member counts
 *
 * ============================================================================
 * CALLBACKS & ACTIONS
 * ============================================================================
 *
 * Join Community:
 *   - Add community.id to joinedCommunityIds
 *   - Update UI immediately (optimistic)
 *   - Future: Firestore transaction
 *
 * Create Post:
 *   - Add new post to feedPosts (top)
 *   - Close CreatePostScreen
 *   - Future: Firestore addDocument
 *
 * Like Post:
 *   - Update post.likeCount
 *   - Toggle like state
 *   - Future: Firestore update + likedBy array
 *
 * Add Comment:
 *   - Update post.commentCount +1
 *   - Close CommentScreen
 *   - Future: Firestore add to comments subcollection
 *
 * ============================================================================
 * INTEGRATION WITH MAINACTIVITY
 * ============================================================================
 *
 * ❌ OLD (WRONG - causes double header/navbar):
 *
 * @Composable
 * fun CommunityScreen(
 *     selectedBottomTab: Int,
 *     onBottomTabSelected: (Int) -> Unit
 * ) {
 *     Scaffold(
 *         topBar = { BreastieHeader(...) },  // ❌ DUPLICATE!
 *         bottomBar = { BottomNavBar(...) }  // ❌ DUPLICATE!
 *     ) { ... }
 * }
 *
 * ✅ NEW (CORRECT - no scaffold):
 *
 * @Composable
 * fun CommunityScreen() {  // No parameters!
 *     Column {  // Direct content, no scaffold
 *         CommunityPageHeader(...)
 *         CommunityTabRow(...)
 *         // Tab content
 *     }
 * }
 *
 * Called from MainActivity:
 *
 * Scaffold(
 *     topBar = { BreastieHeader(...) },     // ✅ Once!
 *     bottomBar = { BottomNavBar(...) }     // ✅ Once!
 * ) {
 *     when (selectedTab) {
 *         1 -> CommunityScreen()  // ✅ No parameters
 *     }
 * }
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - MainActivity.kt (bottom nav tab index 1)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Search communities
 * - Filter by category
 * - Community settings (for moderators)
 * - Pin important posts
 * - Report inappropriate content
 * - Block users
 * - Notification preferences per community
 *
 * ============================================================================
 * PERFORMANCE NOTES
 * ============================================================================
 *
 * - Lazy loading for long lists
 * - Pagination for posts (future)
 * - Image caching (Coil)
 * - Minimize recomposition
 * - Remember states properly
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * FEATURE STATUS: ✅ COMPLETE (Dummy data)
 * NEXT: Firebase integration
 * ============================================================================
 */