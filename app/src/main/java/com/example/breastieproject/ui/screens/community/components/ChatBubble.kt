package com.example.breastieproject.ui.screens.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.ChatMessage
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun ChatBubble(
    message: ChatMessage,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isCurrentUser)
            Arrangement.End else Arrangement.Start
    ) {
        if (!message.isCurrentUser) {
            // Left side: Avatar + Bubble
            Row(
                modifier = Modifier.fillMaxWidth(0.85f)
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
                        text = message.authorUsername.take(2).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Message Bubble
                Column {
                    // Username + Time
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = message.authorUsername,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message.time,
                            fontSize = 10.sp,
                            color = Color(0xFF999999)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Message Content
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 12.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = 12.dp
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = message.message,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        } else {
            // Right side: Bubble only (current user)
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.End
            ) {
                // Time + "Saya"
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.time,
                        fontSize = 10.sp,
                        color = Color(0xFF999999)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Saya",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Message Content (Pink bubble!)
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFFFE4F2),
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 4.dp,
                                    bottomStart = 12.dp,
                                    bottomEnd = 12.dp
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = message.message,
                            fontSize = 14.sp,
                            color = Color(0xFF333333),
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Avatar (right side)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = Color(0xFFEC7FA9),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "R",  // R for "Right" or current user initial
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatBubblePreview() {
    BackupTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF0F8))
                .padding(vertical = 16.dp)
        ) {
            // Others message
            ChatBubble(
                message = ChatMessage(
                    id = "msg_001",
                    authorUsername = "@anonim_user_8472",
                    message = "Selamat pagi semuanya! Bagaimana kabar kalian hari ini? 🌸",
                    time = "09:15",
                    isCurrentUser = false
                )
            )

            // Current user message
            ChatBubble(
                message = ChatMessage(
                    id = "msg_002",
                    authorUsername = "@anonim_user_1234",
                    message = "Pagi semua! Hari ini sesi kemo ku yang ketiga, doain ya 🙏",
                    time = "11:45",
                    isCurrentUser = true
                )
            )
        }
    }
}


/**
 * ============================================================================
 * FILE: ChatBubble.kt
 * LOCATION: ui/screens/community/components/ChatBubble.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Reusable chat bubble component untuk group chat messages.
 * Different styling untuk message dari user vs others.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * message: ChatMessage (REQUIRED)
 *   - Data pesan yang akan ditampilkan
 *   - Contains: sender, text, timestamp, isMe
 *
 * modifier: Modifier (default = Modifier)
 *   - Custom styling dari parent
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Message from Others (isMe = false):
 * ┌─────────────────────────────────┐
 * │ Member A                        │  <- Sender name
 * │ ┌─────────────────────────┐     │
 * │ │ Halo semuanya! 👋       │     │  <- Message bubble (left)
 * │ └─────────────────────────┘     │
 * │   14:30                         │  <- Timestamp
 * └─────────────────────────────────┘
 *
 * Message from Me (isMe = true):
 * ┌─────────────────────────────────┐
 * │                        You      │  <- "You" label
 * │     ┌─────────────────────────┐ │
 * │     │ Halo juga! Gimana kabar?│ │  <- Message bubble (right)
 * │     └─────────────────────────┘ │
 * │                         14:31   │  <- Timestamp
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Messages from Others (Left-aligned):
 *   - Bubble background: #F5F5F5 (light gray)
 *   - Text color: #333333 (dark gray)
 *   - Alignment: Start
 *   - Corner radius: 12.dp (rounded all except top-left)
 *   - Max width: 70% screen
 *
 * Messages from Me (Right-aligned):
 *   - Bubble background: #EC7FA9 (pink)
 *   - Text color: White
 *   - Alignment: End
 *   - Corner radius: 12.dp (rounded all except top-right)
 *   - Max width: 70% screen
 *
 * Sender Name:
 *   - Font: Raleway Medium
 *   - Size: 12.sp
 *   - Color: #666666 (gray)
 *   - Only show for others (not for "You")
 *
 * Message Text:
 *   - Font: Raleway Regular
 *   - Size: 14.sp
 *   - Line height: 20.sp
 *   - Padding: 12.dp
 *
 * Timestamp:
 *   - Font: Raleway Regular
 *   - Size: 10.sp
 *   - Color: #999999 (light gray)
 *   - Format: "HH:mm" (e.g., "14:30")
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Single message
 * ChatBubble(
 *     message = ChatMessage(
 *         id = "msg_001",
 *         communityId = "comm_001",
 *         senderName = "Member A",
 *         message = "Halo!",
 *         timestamp = "14:30",
 *         isMe = false
 *     )
 * )
 *
 * // In LazyColumn (Chat screen)
 * LazyColumn {
 *     items(messages) { message ->
 *         ChatBubble(
 *             message = message,
 *             modifier = Modifier.padding(vertical = 4.dp)
 *         )
 *     }
 * }
 *
 * ============================================================================
 * BEHAVIOR
 * ============================================================================
 *
 * Layout:
 *   - isMe = false: Left-aligned, gray bubble
 *   - isMe = true: Right-aligned, pink bubble
 *   - Auto-scroll to bottom on new message
 *
 * Grouping (Future):
 *   - Consecutive messages from same sender
 *   - Hide sender name for grouped messages
 *   - Reduce spacing between grouped messages
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - ChatScreen.kt (group chat)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Message status (sent, delivered, read)
 * - Reply to message
 * - Long press menu (copy, delete, report)
 * - Link preview
 * - Image/video messages
 * - Voice messages
 * - Emoji reactions
 * - Message editing (with "edited" label)
 * - Message deletion (with "deleted" placeholder)
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Stateless component
 * - Efficient rendering in LazyColumn
 * - Emoji support in text
 * - RTL layout support (future)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */