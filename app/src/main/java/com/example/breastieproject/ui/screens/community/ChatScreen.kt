package com.example.breastieproject.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.ui.screens.community.components.ChatBubble
import com.example.breastieproject.viewmodels.CommunityViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    community: Community,
    viewModel: CommunityViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var messageText by remember { mutableStateOf("") }

    val messages by viewModel.messages.collectAsState()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Load messages when screen opens
    LaunchedEffect(community.id) {
        viewModel.loadMessages(community.id)
    }

    // Cleanup when screen closes
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopListeningToMessages()
        }
    }

    Scaffold(
        topBar = {
            ChatHeader(
                community = community,
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
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = Color(0xFFEC7FA9)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Chat",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = Color(0xFFEC7FA9),
                    unselectedContentColor = Color(0xFF999999)
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Info",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = Color(0xFFEC7FA9),
                    unselectedContentColor = Color(0xFF999999)
                )
            }

            // Tab Content
            when (selectedTab) {
                0 -> ChatTab(
                    community = community,
                    messages = messages,
                    currentUserId = currentUserId,
                    messageText = messageText,
                    onMessageChange = { messageText = it },
                    onSendClick = {
                        viewModel.sendMessage(
                            communityId = community.id,
                            messageText = messageText,
                            onSuccess = {
                                messageText = ""
                            }
                        )
                    }
                )
                1 -> InfoTab(
                    community = community,
                    viewModel = viewModel,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
private fun ChatHeader(
    community: Community,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = community.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${community.memberCount} members",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun ChatTab(
    community: Community,
    messages: List<com.example.breastieproject.data.model.ChatMessage>,
    currentUserId: String,
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto scroll to bottom when messages change
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No messages yet.\nBe the first to say hi! 👋",
                            fontSize = 14.sp,
                            color = Color(0xFF999999),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(
                    items = messages,
                    key = { it.id }
                ) { message ->
                    ChatBubble(
                        message = message,
                        currentUserId = currentUserId
                    )
                }
            }
        }

        // Input Area
        ChatInputField(
            messageText = messageText,
            onMessageChange = onMessageChange,
            onSendClick = onSendClick
        )
    }
}

@Composable
private fun ChatInputField(
    messageText: String,
    onMessageChange: (String) -> Unit,
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
            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp, max = 120.dp),
                placeholder = {
                    Text(
                        text = "Type a message...",
                        color = Color(0xFF999999)
                    )
                },
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

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        onSendClick()
                    }
                },
                enabled = messageText.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = if (messageText.isNotBlank())
                        Color(0xFFEC7FA9) else Color(0xFF999999)
                )
            }
        }
    }
}

@Composable
private fun InfoTab(
    community: Community,
    viewModel: CommunityViewModel,
    onBackClick: () -> Unit
) {
    var showLeaveDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF0F8))
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Community Name",
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                Text(
                    text = community.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                Text(
                    text = community.description,
                    fontSize = 14.sp,
                    color = Color(0xFF555555),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Members",
                    fontSize = 12.sp,
                    color = Color(0xFF999999)
                )
                Text(
                    text = "${community.memberCount} members",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showLeaveDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFDFF0)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Leave Community",
                color = Color(0xFFEC7FA9),
                fontWeight = FontWeight.Bold
            )
        }
    }

    if (showLeaveDialog) {
        AlertDialog(
            onDismissRequest = { showLeaveDialog = false },
            title = {
                Text(
                    text = "Leave Community?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = "Are you sure you want to leave ${community.name}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.leaveCommunity(community.id)
                        showLeaveDialog = false
                        onBackClick()
                    }
                ) {
                    Text(
                        text = "Leave",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false }) {
                    Text(
                        text = "Cancel",
                        color = Color(0xFF999999)
                    )
                }
            }
        )
    }
}


/**
 * ============================================================================
 * FILE: ChatScreen.kt
 * LOCATION: ui/screens/community/ChatScreen.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Fullscreen group chat untuk komunikasi real-time dalam komunitas.
 * WhatsApp-style chat interface dengan message history & input.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * community: Community (REQUIRED)
 *   - Data komunitas untuk chat
 *   - Ditampilkan di header (name, member count)
 *
 * onBackClick: () -> Unit (default = {})
 *   - Callback saat back button diklik
 *   - Close ChatScreen, return to CommunityScreen
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Screen Layout:
 * ┌─────────────────────────────────┐
 * │ ← Pejuang Kemo    1,247 members │  <- Custom header
 * ├─────────────────────────────────┤
 * │                                 │
 * │  Member A                       │  <- Messages
 * │  ┌───────────────┐              │
 * │  │ Halo semua!   │              │
 * │  └───────────────┘              │
 * │    14:30                        │
 * │                                 │
 * │              ┌───────────────┐  │
 * │              │ Halo juga! 👋 │  │
 * │              └───────────────┘  │
 * │                          14:31  │
 * │                                 │
 * │ [Scrollable message list]      │
 * │                                 │
 * ├─────────────────────────────────┤
 * │ [Type message...]        [Send] │  <- Message input
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * COMPONENTS
 * ============================================================================
 *
 * Custom Header:
 *   - Back button (←)
 *   - Community name (Bold, 18.sp)
 *   - Member count (12.sp, gray)
 *   - Background: Pink gradient
 *
 * Message List (LazyColumn):
 *   - Reversed layout (newest at bottom)
 *   - Auto-scroll to bottom on new message
 *   - Pull to load more (future)
 *   - Date separators (future)
 *
 * ChatBubble Component:
 *   - Left-aligned for others (gray)
 *   - Right-aligned for me (pink)
 *   - Sender name + timestamp
 *
 * Message Input Row:
 *   - TextField: Multi-line support
 *   - Send button: IconButton with arrow
 *   - Emoji picker (future)
 *   - Attach image (future)
 *
 * ============================================================================
 * STATE MANAGEMENT
 * ============================================================================
 *
 * Local States:
 *
 * messages: List<ChatMessage>
 *   - Current chat messages
 *   - Load from DummyChatData (dummy)
 *   - Future: Real-time Firestore listener
 *
 * messageText: String
 *   - Input field text
 *   - Mutable via TextField
 *   - Clear after send
 *
 * listState: LazyListState
 *   - Control scroll position
 *   - Auto-scroll to bottom on new message
 *
 * ============================================================================
 * INTERACTIONS
 * ============================================================================
 *
 * Send Message:
 *   1. Validate messageText not empty
 *   2. Create new ChatMessage
 *   3. Add to messages list (bottom)
 *   4. Clear input field
 *   5. Scroll to bottom
 *   6. Future: Send to Firestore
 *
 * Back Button:
 *   1. Call onBackClick()
 *   2. CommunityScreen handles navigation
 *   3. ChatScreen removed from composition
 *
 * Scroll Behavior:
 *   - Auto-scroll when new message arrives
 *   - Manual scroll up to read history
 *   - "Scroll to bottom" FAB (future)
 *
 * ============================================================================
 * DATA SOURCE
 * ============================================================================
 *
 * Current (Dummy):
 *
 * DummyChatData.getMessagesForCommunity(communityId)
 *   - Returns List<ChatMessage>
 *   - Static data for testing
 *
 * Future (Firebase):
 *
 * Firestore Path: "chats/{communityId}/messages"
 *
 * Real-time Listener:
 * ```
 * firestore.collection("chats")
 *     .document(communityId)
 *     .collection("messages")
 *     .orderBy("timestamp", Query.Direction.ASCENDING)
 *     .addSnapshotListener { snapshot, error ->
 *         // Update messages list
 *     }
 * ```
 *
 * Send Message:
 * ```
 * val message = ChatMessage(
 *     id = UUID.randomUUID().toString(),
 *     communityId = community.id,
 *     senderId = currentUserId,
 *     senderName = currentUserName,
 *     message = messageText,
 *     timestamp = System.currentTimeMillis(),
 *     isMe = true
 * )
 *
 * firestore.collection("chats")
 *     .document(communityId)
 *     .collection("messages")
 *     .add(message)
 * ```
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // From CommunityScreen
 * var showChat by remember { mutableStateOf(false) }
 * var selectedCommunity by remember { mutableStateOf<Community?>(null) }
 *
 * if (showChat && selectedCommunity != null) {
 *     ChatScreen(
 *         community = selectedCommunity!!,
 *         onBackClick = {
 *             showChat = false
 *             selectedCommunity = null
 *         }
 *     )
 * }
 *
 * // Trigger from community card click
 * CommunityCard(
 *     community = community,
 *     onCardClick = {
 *         selectedCommunity = community
 *         showChat = true
 *     }
 * )
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Header:
 *   - Height: 64.dp
 *   - Background: Gradient pink
 *   - Back icon: White, 24.dp
 *   - Community name: White, Raleway Bold, 18.sp
 *   - Member count: White 70% opacity, 12.sp
 *
 * Message List:
 *   - Background: #F8F8F8 (light gray)
 *   - Padding: 16.dp horizontal
 *   - Spacing: 8.dp vertical
 *
 * Input Row:
 *   - Height: 56.dp
 *   - Background: White
 *   - Elevation: 4.dp
 *   - TextField: Raleway Regular, 14.sp
 *   - Send button: Pink circle, white icon
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - CommunityScreen.kt (modal overlay)
 *
 * Called from:
 * - MyCommunityTab.kt (community card click)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Typing indicator ("Member A is typing...")
 * - Online status indicators
 * - Read receipts (checkmarks)
 * - Message reactions (emoji)
 * - Reply to specific message
 * - Forward message
 * - Delete message
 * - Edit message (with "edited" label)
 * - Search in chat
 * - Pin messages
 * - Mute notifications
 * - Report inappropriate messages
 * - Voice messages
 * - Image/video sharing
 * - GIF support
 * - Link previews
 *
 * ============================================================================
 * PERFORMANCE NOTES
 * ============================================================================
 *
 * - Lazy loading old messages (pagination)
 * - Limit initial load to 50 messages
 * - Load more on scroll to top
 * - Optimize recomposition (key in LazyColumn)
 * - Image caching for avatars
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * STATUS: ✅ COMPLETE (UI only)
 * NEXT: Real-time Firebase chat
 * ============================================================================
 */