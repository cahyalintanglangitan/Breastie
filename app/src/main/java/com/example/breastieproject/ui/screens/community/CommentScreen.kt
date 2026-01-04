package com.example.breastieproject.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.viewmodels.CommunityViewModel
import kotlinx.coroutines.launch

@Composable
fun CommentScreen(
    post: Post,
    viewModel: CommunityViewModel = viewModel(),  // ✅ ADD
    onBackClick: () -> Unit = {}
) {
    // Load comments for this post
    LaunchedEffect(post.id) {
        viewModel.loadComments(post.id)
    }

    val comments by viewModel.comments.collectAsState()
    var commentText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CommentHeader(
                commentCount = comments.size,
                onBackClick = onBackClick
            )
        },
        containerColor = Color(0xFFFFF0F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Original Post
                item {
                    OriginalPostCard(post = post)
                }

                // Divider
                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        color = Color(0xFFFFDFF0)
                    )
                }

                // Comments Section Header
                item {
                    Text(
                        text = "Comments (${comments.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Comments
                if (comments.isEmpty()) {
                    item {
                        EmptyComments()
                    }
                } else {
                    items(
                        items = comments,
                        key = { it.id }
                    ) { comment ->
                        CommentBubble(comment = comment)
                    }
                }
            }

            // Input Area
            CommentInputField(
                commentText = commentText,
                onCommentChange = { commentText = it },
                isEnabled = !isSending,
                onSendClick = {
                    if (commentText.isNotBlank()) {
                        isSending = true

                        // Call ViewModel to add comment
                        viewModel.addComment(
                            postId = post.id,
                            commentText = commentText.trim(),
                            onSuccess = {
                                commentText = ""
                                isSending = false

                                // Scroll to bottom
                                coroutineScope.launch {
                                    listState.animateScrollToItem(comments.size + 2)
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun CommentHeader(
    commentCount: Int,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFB8E0),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Comments",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Text(
                text = "($commentCount)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
private fun OriginalPostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color(0xFF333333),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Stats
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "❤️ ${post.likes}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = "💬 ${post.commentCount}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
private fun CommentBubble(comment: com.example.breastieproject.data.model.Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color(0xFFFFB8E0),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = comment.authorUsername.take(1).uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Comment Content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Username + Time
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.authorUsername,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.timeAgo,
                    fontSize = 10.sp,
                    color = Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Comment Text
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Text(
                    text = comment.comment,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyComments() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No comments yet",
            fontSize = 14.sp,
            color = Color(0xFF999999)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Be the first to comment!",
            fontSize = 12.sp,
            color = Color(0xFFBBBBBB)
        )
    }
}

@Composable
private fun CommentInputField(
    commentText: String,
    onCommentChange: (String) -> Unit,
    isEnabled: Boolean = true,
    onSendClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Text Input
            OutlinedTextField(
                value = commentText,
                onValueChange = onCommentChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp, max = 120.dp),
                placeholder = {
                    Text(
                        text = "Write a comment...",
                        color = Color(0xFF999999)
                    )
                },
                enabled = isEnabled,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFF0F8),
                    unfocusedContainerColor = Color(0xFFFFF0F8),
                    focusedBorderColor = Color(0xFFFFB8E0),
                    unfocusedBorderColor = Color(0xFFFFDFF0),
                    focusedTextColor = Color(0xFF333333),
                    unfocusedTextColor = Color(0xFF333333),
                    cursorColor = Color(0xFFEC7FA9)
                ),
                shape = RoundedCornerShape(24.dp),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Send Button
            IconButton(
                onClick = {
                    if (commentText.isNotBlank()) {
                        onSendClick()
                    }
                },
                enabled = commentText.isNotBlank() && isEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (commentText.isNotBlank() && isEnabled)
                        Color(0xFFEC7FA9) else Color(0xFF999999)
                )
            }
        }
    }
}