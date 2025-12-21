package com.example.breastieproject.ui.screens.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun PostCard(
    post: Post,
    isLiked: Boolean = false,
    currentUserId: String = "",  // ✅ ADD THIS!
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},  // ✅ ADD THIS!
    modifier: Modifier = Modifier
) {

    var showDeleteDialog by remember { mutableStateOf(false) }
    val isOwnPost = post.authorId == currentUserId

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Avatar + Username + Community + Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar (placeholder circle)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = Color(0xFFFFB8E0),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorUsername.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Username + Community + Time
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorUsername,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "from: ${post.communityName}",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                        Text(
                            text = " • ${post.createdAt}",
                            fontSize = 12.sp,
                            color = Color(0xFF999999)
                        )
                    }
                }

                if (isOwnPost) {
                    IconButton(
                        onClick = { showDeleteDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFF999999)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content (Text only!)
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: Like, Comment,
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // ✅ UPDATED LIKE BUTTON:
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onLikeClick  // ✅ CHANGED: Remove toggle logic!
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color.Red else Color(0xFF999999)  // ✅ CHANGED: Red when liked!
                        )
                    }
                    Text(
                        text = "${post.likes}",  // ✅ CHANGED: Just show actual count
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Comment Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    TextButton(onClick = onCommentClick) {
                        Text(
                            text = "💬 ${post.commentCount}",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }

            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Post?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = "This action cannot be undone. Are you sure you want to delete this post?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text(
                        text = "Cancel",
                        color = Color(0xFF999999)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun PostCardPreview() {
    BackupTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEDFA))
                .padding(vertical = 16.dp)
        ) {
            PostCard(
                post = Post(
                    id = "post_001",
                    communityName = "Pejuang Kemo",
                    authorUsername = "@anonim_user_8472",
                    content = "Hari ini aku merasa lebih baik setelah sesi kemo kemarin. Terima kasih untuk semua dukungannya! 💪",
                    likes = 45,
                    commentCount = 12,
                ),
                isLiked = false  // ✅ ADD FOR PREVIEW
            )

            PostCard(
                post = Post(
                    id = "post_002",
                    communityName = "Survivor Stories",
                    authorUsername = "@anonim_user_9123",
                    content = "Selamat pagi semua! Hari ini adalah hari ke-365 sejak remisi ku. Terima kasih untuk semua yang sudah support ❤️🎉",
                    likes = 134,
                    commentCount = 45,
                ),
                isLiked = true  // ✅ ADD FOR PREVIEW (liked state)
            )
        }
    }
}


/**
 * ============================================================================
 * FILE: PostCard.kt
 * LOCATION: ui/screens/community/components/PostCard.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Reusable card component untuk display post di community feed.
 * Social media style card dengan like, comment, actions.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * post: Post (REQUIRED)
 *   - Data post yang akan ditampilkan
 *   - Contains: author, content, likes, comments, dll
 *
 * onLikeClick: (Post) -> Unit (default = {})
 *   - Callback saat like button diklik
 *   - Handle like/unlike logic
 *   - Update likeCount
 *
 * onCommentClick: (Post) -> Unit (default = {})
 *   - Callback saat comment button diklik
 *   - Navigate to CommentScreen
 *   - Pass post data
 *
 * modifier: Modifier (default = Modifier)
 *   - Custom styling dari parent
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Card Layout:
 * ┌─────────────────────────────────┐
 * │ ●  Member A                     │  <- Avatar + Name
 * │    Pejuang Kemo · 2 jam lalu    │  <- Community + Time
 * │                                 │
 * │ Post content text here...       │  <- Content (expandable)
 * │ Multiple lines supported.       │
 * │                                 │
 * │ [Optional Image]                │  <- Image attachment
 * │                                 │
 * │ ❤️ 24    💬 8                   │  <- Like, Comment,  counts
 * │                                 │
 * │ ❤️ Like  💬 Comment             │  <- Action buttons
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Card:
 *   - Background: White
 *   - Corner radius: 12.dp
 *   - Elevation: 1.dp
 *   - Padding: 16.dp
 *   - Margin: 8.dp vertical
 *
 * Header Section:
 *   - Avatar: 40.dp circle, pink background
 *   - Author name: Raleway SemiBold, 14.sp, Black
 *   - Community + Time: Raleway Regular, 12.sp, Gray
 *
 * Content Section:
 *   - Text: Raleway Regular, 14.sp, Dark Gray
 *   - Line height: 20.sp
 *   - Max lines: None (full content)
 *
 * Image (if exists):
 *   - Width: fillMaxWidth
 *   - Height: 200.dp
 *   - Corner radius: 8.dp
 *   - Content scale: Crop
 *
 * Stats Row:
 *   - Icons: 16.dp
 *   - Text: 12.sp, Gray
 *   - Spacing: 16.dp between items
 *
 * Action Buttons:
 *   - Height: 40.dp
 *   - Icon + Text
 *   - Ripple effect
 *   - Colors:
 *     - Default: Gray
 *     - Active (liked): Pink (#EC7FA9)
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Basic usage
 * PostCard(
 *     post = myPost,
 *     onLikeClick = { post ->
 *         viewModel.toggleLike(post.id)
 *     },
 *     onCommentClick = { post ->
 *         navController.navigate("comment/${post.id}")
 *     },
 * )
 *
 * // In LazyColumn (Feed)
 * LazyColumn {
 *     items(posts) { post ->
 *         PostCard(
 *             post = post,
 *             onLikeClick = { /* ... */ },
 *             onCommentClick = { /* ... */ },
 *             modifier = Modifier.padding(horizontal = 16.dp)
 *         )
 *     }
 * }
 *
 * ============================================================================
 * INTERACTIONS
 * ============================================================================
 *
 * Like Button:
 *   - Tap: Toggle like/unlike
 *   - Visual: Icon color changes (gray → pink)
 *   - Count: +1 or -1 immediately
 *   - Animation: Heart scale (future)
 *
 * Comment Button:
 *   - Tap: Navigate to CommentScreen
 *   - Pass: post.id
 *   - Show: All comments for this post
 *
 *
 * Content:
 *   - Long content: "Read more" (future)
 *   - Links: Clickable (future)
 *   - Mentions: Highlighted (future)
 *
 * ============================================================================
 * STATE MANAGEMENT
 * ============================================================================
 *
 * Local State:
 *   - isLiked: Boolean (track user's like)
 *   - isExpanded: Boolean (for long content)
 *
 * Future (Firebase):
 *   - Real-time like count updates
 *   - Optimistic UI updates
 *   - Sync with backend
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - FeedTab.kt (main feed)
 * - CommentScreen.kt (original post view)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Multiple image support (gallery)
 * - Video attachment support
 * - Link preview cards
 * - Emoji reactions (beyond like)
 * - Edit/delete options (for author)
 * - Report/block options
 * - Save post bookmark
 * - Long press context menu
 *
 * ============================================================================
 * ACCESSIBILITY
 * ============================================================================
 *
 * - Semantic content descriptions
 * - TalkBack support
 * - Minimum touch target: 48.dp
 * - High contrast mode support
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Stateless component (pure UI)
 * - Parent handles business logic
 * - Optimized for LazyColumn (recomposition)
 * - Image loading via Coil library
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */