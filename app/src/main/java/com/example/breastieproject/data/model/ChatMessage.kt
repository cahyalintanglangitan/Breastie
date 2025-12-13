package com.example.breastieproject.data.model

data class ChatMessage(
    val id: String = "",
    val communityId: String = "",
    val authorId: String = "",
    val authorUsername: String = "",  // @anonim_user_8472
    val message: String = "",
    val timestamp: Long = 0,
    val time: String = "",  // "09:15"
    val isCurrentUser: Boolean = false  // true = current user (right bubble)
)

/**
 * ============================================================================
 * FILE: ChatMessage.kt
 * LOCATION: data/model/ChatMessage.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Data class untuk merepresentasikan pesan dalam group chat komunitas.
 * Real-time messaging antar member dalam satu komunitas.
 *
 * ============================================================================
 * PROPERTIES
 * ============================================================================
 *
 * id: String - Unique identifier message
 * communityId: String - ID komunitas tempat chat
 * senderName: String - Nama pengirim (anonymized)
 * senderAvatar: String - Avatar pengirim
 * message: String - Isi pesan text
 * timestamp: String - Waktu pengiriman ("14:30")
 * isMe: Boolean - true jika pesan dari current user
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * val message = ChatMessage(
 *     id = "msg_001",
 *     communityId = "comm_001",
 *     senderName = "Member A",
 *     senderAvatar = "",
 *     message = "Halo semuanya!",
 *     timestamp = "14:30",
 *     isMe = false
 * )
 *
 * // Check if message from me
 * if (message.isMe) {
 *     // Align right, different color
 * }
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - DummyChatData.kt
 * - ChatScreen.kt
 * - ChatBubble.kt (UI component)
 *
 * ============================================================================
 * FIREBASE REAL-TIME (FUTURE)
 * ============================================================================
 *
 * Collection: "chats/{communityId}/messages"
 *
 * Real-time listener:
 * - Listen for new messages
 * - Auto-scroll to bottom
 * - Show "typing..." indicator
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */