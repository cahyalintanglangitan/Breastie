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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Comment
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.data.repository.dummy.DummyCommentData
import com.example.breastieproject.data.repository.dummy.DummyPostData
import com.example.breastieproject.ui.theme.BackupTheme
import kotlinx.coroutines.launch

@Composable
fun CommentScreen(
    post: Post,
    onBackClick: () -> Unit = {},
    onCommentAdded: () -> Unit = {}
) {
    var commentText by remember { mutableStateOf("") }
    var comments by remember {
        mutableStateOf(DummyCommentData.getCommentsByPost(post.id).toMutableList())
    }

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
                    Divider(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        color = Color(0xFFFFDFF0)
                    )
                }

                // Comments Section Header
                item {
                    Text(
                        text = "Komentar (${comments.size})",
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
                onSendClick = {
                    if (commentText.isNotBlank()) {
                        // Create new comment
                        val newComment = Comment(
                            id = "comment_${System.currentTimeMillis()}",
                            postId = post.id,
                            authorId = "user_123",
                            authorUsername = "@anonim_user_1234",
                            comment = commentText.trim(),
                            timestamp = System.currentTimeMillis(),
                            createdAt = "Baru saja"
                        )

                        // Add to list
                        comments.add(newComment)
                        DummyCommentData.addComment(newComment)

                        // Clear input
                        commentText = ""

                        // Scroll to bottom
                        coroutineScope.launch {
                            listState.animateScrollToItem(comments.size + 2) // +2 for post and header
                        }

                        onCommentAdded()
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
                    text = "Komentar",
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
private fun OriginalPostCard(
    post: Post
) {
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
private fun CommentBubble(
    comment: Comment
) {
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
                    text = comment.createdAt,
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
            text = "Belum ada komentar",
            fontSize = 14.sp,
            color = Color(0xFF999999)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Jadilah yang pertama berkomentar!",
            fontSize = 12.sp,
            color = Color(0xFFBBBBBB)
        )
    }
}

@Composable
private fun CommentInputField(
    commentText: String,
    onCommentChange: (String) -> Unit,
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
                        text = "Tulis komentar...",
                        color = Color(0xFF999999)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFF0F8),
                    unfocusedContainerColor = Color(0xFFFFF0F8),
                    focusedBorderColor = Color(0xFFFFB8E0),
                    unfocusedBorderColor = Color(0xFFFFDFF0),
                    focusedTextColor = Color(0xFF333333),      // Hitam!
                    unfocusedTextColor = Color(0xFF333333),    // Hitam!
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
                enabled = commentText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (commentText.isNotBlank())
                        Color(0xFFEC7FA9) else Color(0xFF999999)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CommentScreenPreview() {
    BackupTheme {
        CommentScreen(
            post = DummyPostData.posts[0]
        )
    }
}