package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.ChatMessage

object DummyChatData {

    // Current user ID (dummy)
    const val CURRENT_USER_ID = "user_123"
    const val CURRENT_USER_USERNAME = "@anonim_user_1234"

    // Dummy messages for "Pejuang Kemo" community
    private val pejuangKemoMessages = listOf(
        ChatMessage(
            id = "msg_001",
            communityId = "comm_001",
            authorId = "user_456",
            authorUsername = "@anonim_user_8472",
            message = "Selamat pagi semuanya! Bagaimana kabar kalian hari ini? 🌸",
            timestamp = System.currentTimeMillis() - (7 * 60 * 60 * 1000),
            time = "09:15",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_002",
            communityId = "comm_001",
            authorId = "user_789",
            authorUsername = "@anonim_user_5678",
            message = "Pagi kak Sarah! Alhamdulillah baik. Semangat untuk kita semua! 😊",
            timestamp = System.currentTimeMillis() - (6 * 60 * 60 * 1000),
            time = "10:30",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_003",
            communityId = "comm_001",
            authorId = CURRENT_USER_ID,
            authorUsername = CURRENT_USER_USERNAME,
            message = "Pagi semua! Hari ini sesi kemo ku yang ketiga, doain ya 🙏",
            timestamp = System.currentTimeMillis() - (5 * 60 * 60 * 1000),
            time = "11:45",
            isCurrentUser = true  // Current user!
        ),
        ChatMessage(
            id = "msg_004",
            communityId = "comm_001",
            authorId = "user_234",
            authorUsername = "@anonim_user_3456",
            message = "Aku mau tanya dong, ada yang punya tips mengatasi mual setelah kemo?",
            timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000),
            time = "14:20",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_005",
            communityId = "comm_001",
            authorId = "user_234",
            authorUsername = "@anonim_user_3456",
            message = "Kemarin aku coba minum air jahe tapi masih kurang efektif",
            timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000) + 60000,
            time = "14:21",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_006",
            communityId = "comm_001",
            authorId = "user_567",
            authorUsername = "@anonim_user_7890",
            message = "Coba makan makanan dingin kak, kayak es krim atau buah dingin. Aku kemarin gitu dan lumayan membantu",
            timestamp = System.currentTimeMillis() - (90 * 60 * 1000),
            time = "15:15",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_007",
            communityId = "comm_001",
            authorId = "user_789",
            authorUsername = "@anonim_user_5678",
            message = "SEMOGA LEKAS MEMBAIK YA KAK! 💪❤️",
            timestamp = System.currentTimeMillis() - (30 * 60 * 1000),
            time = "16:00",
            isCurrentUser = false
        ),
        ChatMessage(
            id = "msg_008",
            communityId = "comm_001",
            authorId = CURRENT_USER_ID,
            authorUsername = CURRENT_USER_USERNAME,
            message = "Terima kasih semuanya untuk supportnya! 🥰",
            timestamp = System.currentTimeMillis() - (10 * 60 * 1000),
            time = "16:20",
            isCurrentUser = true
        )
    )

    // Function: Get messages by community ID
    fun getMessagesByCommunity(communityId: String): List<ChatMessage> {
        return when (communityId) {
            "comm_001" -> pejuangKemoMessages
            // Add more communities later
            else -> emptyList()
        }.sortedBy { it.timestamp }  // Sort by time (oldest first)
    }
}