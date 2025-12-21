package com.example.breastieproject.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.breastieproject.viewmodels.CommunityViewModel

@Composable
fun CreatePostScreen(
    viewModel: CommunityViewModel = viewModel(),  // ✅ ADD
    onBackClick: () -> Unit = {}
) {
    // Get joined communities from ViewModel
    val allCommunities by viewModel.allCommunities.collectAsState()
    val joinedCommunityIds by viewModel.joinedCommunityIds.collectAsState()
    val joinedCommunities = allCommunities.filter { it.id in joinedCommunityIds }

    var postContent by remember { mutableStateOf("") }
    var selectedCommunity by remember { mutableStateOf<Community?>(joinedCommunities.firstOrNull()) }
    var showCommunityDropdown by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }

    val maxCharacters = 500
    val canPost = postContent.isNotBlank() &&
            postContent.length <= maxCharacters &&
            selectedCommunity != null &&
            !isPosting

    Scaffold(
        topBar = {
            CreatePostHeader(
                onBackClick = onBackClick,
                onPostClick = {
                    if (canPost && selectedCommunity != null) {
                        isPosting = true

                        // Call ViewModel to create post
                        viewModel.createPost(
                            communityId = selectedCommunity!!.id,
                            communityName = selectedCommunity!!.name,
                            content = postContent.trim(),
                            onSuccess = {
                                isPosting = false
                                onBackClick()  // Close screen on success
                            }
                        )
                    } else if (postContent.isBlank()) {
                        errorMessage = "Post cannot be empty!"
                        showErrorDialog = true
                    } else if (selectedCommunity == null) {
                        errorMessage = "Please select a community first!"
                        showErrorDialog = true
                    }
                },
                canPost = canPost
            )
        },
        containerColor = Color(0xFFFFF0F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Community Selector
            Text(
                text = "Select Community",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Button
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                onClick = { showCommunityDropdown = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCommunity?.name ?: "Select community",
                        fontSize = 16.sp,
                        color = if (selectedCommunity != null)
                            Color(0xFF333333) else Color(0xFF999999)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color(0xFF999999)
                    )
                }
            }

            // Community Dropdown Menu
            DropdownMenu(
                expanded = showCommunityDropdown,
                onDismissRequest = { showCommunityDropdown = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                if (joinedCommunities.isEmpty()) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "No communities joined yet",
                                fontSize = 14.sp,
                                color = Color(0xFF999999)
                            )
                        },
                        onClick = { }
                    )
                } else {
                    joinedCommunities.forEach { community ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = community.name,
                                    fontSize = 16.sp,
                                    color = Color(0xFF333333)
                                )
                            },
                            onClick = {
                                selectedCommunity = community
                                showCommunityDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Post Content Input
            Text(
                text = "Post Content",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = postContent,
                onValueChange = {
                    if (it.length <= maxCharacters) {
                        postContent = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp),
                placeholder = {
                    Text(
                        text = "Write something...\n\nShare your experience, ask questions, or give support to fellow community members.",
                        color = Color(0xFF999999),
                        fontSize = 14.sp
                    )
                },
                enabled = !isPosting,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFFEC7FA9),
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    focusedTextColor = Color(0xFF333333),
                    unfocusedTextColor = Color(0xFF333333),
                    cursorColor = Color(0xFFEC7FA9)
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Character Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${postContent.length}/$maxCharacters characters",
                    fontSize = 12.sp,
                    color = if (postContent.length >= maxCharacters)
                        Color.Red else Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Guidelines Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE4F2)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "💡 Posting Tips",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = """
                            • Be kind and supportive
                            • Respect others' privacy
                            • Share positive experiences
                            • Avoid misleading medical information
                        """.trimIndent(),
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        lineHeight = 18.sp
                    )
                }
            }

            // Loading indicator
            if (isPosting) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFEC7FA9)
                )
            }
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Attention",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(
                        text = "OK",
                        color = Color(0xFFEC7FA9)
                    )
                }
            }
        )
    }
}

@Composable
private fun CreatePostHeader(
    onBackClick: () -> Unit,
    onPostClick: () -> Unit,
    canPost: Boolean
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
            // Back Button
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
                    text = "Create Post",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Post Button
            TextButton(
                onClick = onPostClick,
                enabled = canPost,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Post",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}