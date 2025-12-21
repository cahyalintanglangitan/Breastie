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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.ui.screens.community.tabs.ExploreTab
import com.example.breastieproject.ui.screens.community.tabs.FeedTab
import com.example.breastieproject.ui.screens.community.tabs.MyCommunityTab
import com.example.breastieproject.ui.screens.webinar.WebinarDetailScreen
import com.example.breastieproject.viewmodels.CommunityViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }

    // Observe ViewModel states
    val allCommunities by viewModel.allCommunities.collectAsState()
    val joinedCommunityIds by viewModel.joinedCommunityIds.collectAsState()
    val feedPosts by viewModel.feedPosts.collectAsState()
    val likedPostIds by viewModel.likedPostIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // State for navigation
    var selectedCommunity by remember { mutableStateOf<Community?>(null) }
    var showChat by remember { mutableStateOf(false) }
    var showWebinar by remember { mutableStateOf(false) }
    var showCreatePost by remember { mutableStateOf(false) }

    // State for comment screen
    var selectedPost by remember { mutableStateOf<Post?>(null) }
    var showComments by remember { mutableStateOf(false) }

    // Show CommentScreen
    if (showComments && selectedPost != null) {
        CommentScreen(
            post = selectedPost!!,
            viewModel = viewModel,
            onBackClick = {
                showComments = false
                selectedPost = null
            }
        )
    }
    // Show CreatePostScreen
    else if (showCreatePost) {
        CreatePostScreen(
            viewModel = viewModel,
            onBackClick = { showCreatePost = false }
        )
    }
    // Show WebinarScreen
    else if (showWebinar) {
        WebinarDetailScreen(
            onBackClick = { showWebinar = false }  // ✅ ONLY THIS!
        )
    }
    // Show ChatScreen
    else if (showChat && selectedCommunity != null) {
        ChatScreen(
            community = selectedCommunity!!,
            viewModel = viewModel,
            onBackClick = {
                showChat = false
                selectedCommunity = null
            }
        )
    }
    // Show normal CommunityScreen
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFEDFA))
        ) {
            // Page Header
            CommunityPageHeader(
                onCreatePostClick = { showCreatePost = true }
            )

            // Tab Row
            CommunityTabRow(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            // Show loading
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFEC7FA9)
                    )
                }
            } else {
                // Tab Content
                when (selectedTab) {
                    0 -> {
                        // My Community: Show only joined
                        val joinedCommunities = allCommunities.filter {
                            it.id in joinedCommunityIds
                        }

                        MyCommunityTab(
                            communities = joinedCommunities,
                            onCommunityClick = { community ->
                                selectedCommunity = community
                                showChat = true
                            },
                            onBannerClick = { showWebinar = true }
                        )
                    }
                    1 -> {
                        // Explore: Show only NOT joined
                        val availableCommunities = allCommunities.filter {
                            it.id !in joinedCommunityIds
                        }

                        ExploreTab(
                            communities = availableCommunities,
                            onJoinCommunity = { community ->
                                viewModel.joinCommunity(community.id)
                            },
                            onBannerClick = { showWebinar = true }
                        )
                    }
                    2 -> {
                        val joinedCommunities = allCommunities.filter {
                            it.id in joinedCommunityIds
                        }

                        FeedTab(
                            posts = feedPosts,
                            likedPostIds = likedPostIds,
                            currentUserId = currentUserId,
                            joinedCommunities = joinedCommunities,  // ✅ ADD THIS!
                            onLikeClick = { post ->
                                viewModel.toggleLikePost(post.id)
                            },
                            onCommentClick = { post ->
                                selectedPost = post
                                showComments = true
                            },
                            onDeleteClick = { post ->
                                viewModel.deletePost(post.id)
                            },
                            onBannerClick = { showWebinar = true }
                        )
                    }
                }
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
                    text = "Connect with people who understand your journey",
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
                    text = "New Post",
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